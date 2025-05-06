package com.wwdui.springboot3_modle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwdui.springboot3_modle.config.GoodElasticsearchRepository;
import com.wwdui.springboot3_modle.mapper.GoodMapper;
import com.wwdui.springboot3_modle.mapper.UserGoodMapper;
import com.wwdui.springboot3_modle.pojo.Good;
import com.wwdui.springboot3_modle.pojo.LoginUser;
import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.UserGood;
import com.wwdui.springboot3_modle.service.GoodService;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private UserGoodMapper userGoodMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private GoodElasticsearchRepository goodElasticsearchRepository;
    @Override
    @Transactional
    public Result<?> buyGoodById(long goodId, int buyNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        String lockKey = "good:lock:" + goodId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁（非阻塞）
            // waitTime: 等待时间，设置为 0 表示不等待
            // leaseTime: 锁的持有时间，防止死锁
            boolean isLocked = lock.tryLock(0, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                return Result.error(500, "系统繁忙，请稍后重试");
            }


            ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
            Integer stock = ops.get("good:stock:" + goodId);
            // 如果 Redis 中没有库存数据，从数据库中查询并缓存到 Redis
            if (stock == null) {
                Good good = goodMapper.selectById(goodId);
                if (good == null) {
                    return Result.error(404, "商品不存在");
                }
                stock = good.getGoodNum();
                ops.set("good:stock:" + goodId, stock);
            }
            // 检查库存是否足够
            if (stock < buyNum) {
                return Result.error(400, "库存不足");
            }
            // 减少库存并更新数据库
            Good good = goodMapper.selectById(goodId);
            good.setGoodNum(good.getGoodNum() - buyNum);
            goodMapper.updateById(good);

            //同步数据到Elasticsearch
            goodElasticsearchRepository.save(good);

            //原子性地对键的值进行操作
            ops.decrement("good:stock:" + goodId, buyNum);

            recordUserGood(userid, goodId, buyNum);

            return Result.success();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.error(500, "系统异常，请重试");
        } finally {
            // 释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public List<Good> searchProducts(String query) {
        return goodElasticsearchRepository.findByGoodName(query);
    }

    /**
     * 记录用户购买信息
     *
     * @param userid 用户ID
     * @param goodId 商品ID
     * @param buyNum 购买数量
     */
    private void recordUserGood(Long userid, long goodId, int buyNum) {
        // 查询是否已有购买记录
        UserGood userGood = userGoodMapper.selectOne(new QueryWrapper<UserGood>()
                .eq("user_id", userid)
                .eq("good_id", goodId));

        if (userGood == null) {
            // 如果没有记录，插入新记录
            userGood = new UserGood();
            userGood.setUserId(userid);
            userGood.setGoodId(goodId);
            userGood.setBuyNum(buyNum);
            userGoodMapper.insert(userGood);
        } else {
            // 如果有记录，更新购买数量
            userGood.setBuyNum(userGood.getBuyNum() + buyNum);
            userGoodMapper.updateById(userGood);
        }
    }
}
