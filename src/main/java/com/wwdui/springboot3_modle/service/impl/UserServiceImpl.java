package com.wwdui.springboot3_modle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwdui.springboot3_modle.mapper.UserMapper;
import com.wwdui.springboot3_modle.pojo.LoginUser;
import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.User;
import com.wwdui.springboot3_modle.service.UserService;
import com.wwdui.springboot3_modle.util.JwtUtil;
import com.wwdui.springboot3_modle.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    //注册
    @Override
    public User registerUser(User user) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        if (userMapper.selectOne(queryWrapper)!= null){
            throw new RuntimeException("用户名已存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.insert(user);
        return user;
    }

    //登录
    @Override
    public Result<?> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        loginUser.setToken(jwt);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser,1, TimeUnit.HOURS);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);

        System.out.println(loginUser);
        return Result.success(map);
    }

    //登出
    @Override
    public Result<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return  Result.success();
    }

    //信息
    @Override
    public Result<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        String email=loginUser.getUser().getEmail();
        String name=loginUser.getUser().getName();
        String username = loginUser.getUser().getUsername();
        HashMap<String,Object> map = new HashMap<>();
        map.put("userid",userid);
        map.put("username",username);
        map.put("name",name);
        map.put("email",email);
        return Result.success(map);
    }





}
