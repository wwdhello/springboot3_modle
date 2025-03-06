package com.wwdui.springboot3_modle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wwdui.springboot3_modle.mapper.FileMapper;
import com.wwdui.springboot3_modle.pojo.FileEntity;
import com.wwdui.springboot3_modle.pojo.LoginUser;
import com.wwdui.springboot3_modle.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private FileMapper fileMapper;
    @Override
    public String checkFileExists(String md5) {
        // 从 Redis 中查询
        String filePath = redisTemplate.opsForValue().get("file:md5:" + md5);
        if (filePath != null) {
            return filePath;
        }
        // 从数据库中查询
        FileEntity fileEntity=fileMapper.selectOne(new QueryWrapper<FileEntity>()
                .eq("md5",md5));
        if (fileEntity!=null){
            //存入redis
            redisTemplate.opsForValue().set("file:md5:" + md5, fileEntity.getFilePath(),1, TimeUnit.HOURS);
            return fileEntity.getFilePath();
        }
        return null;
    }

    @Override
    public boolean uploadChunk(MultipartFile multipartFile, String md5, int chunkIndex, int totalChunks) {
        // 保存分片逻辑（不同浏览器有不同的方法）
        // ...


        //获取用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userId=loginUser.getUser().getId();
        // 如果是最后一个分片，触发合并
        if (chunkIndex==totalChunks-1){
            mergeChunks(md5,totalChunks,userId);
        }
        return true;
    }


    @Override
    public PageInfo<FileEntity> listFilePathsByUserId( Integer pageNum, Integer pageSize) {

        //获取用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("用户未登录或登录信息无效");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userId=loginUser.getUser().getId();
        // 使用 PageHelper 分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询数据
        Page<FileEntity> page = fileMapper.selectFilePathsByUserId(userId);
        // 返回分页结果
        return new PageInfo<>(page);
    }

    private void mergeChunks(String md5, int totalChunks, long userId) {
        // 合并分片逻辑（不同浏览器有不同的方法）
        String filePath=" ";
        Date createdTime=new Date();
        // ...

        // 将文件信息保存到数据库和 Redis
        FileEntity fileEntity=new FileEntity();
        fileEntity.setMd5(md5);
        fileEntity.setFilePath(filePath);
        fileEntity.setUserId(userId);
        fileEntity.getCreatedTime(/**时间设置*/);
        fileMapper.insert(fileEntity);
    }


}
