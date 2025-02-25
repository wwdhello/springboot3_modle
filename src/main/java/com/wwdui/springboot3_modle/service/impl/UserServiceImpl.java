package com.wwdui.springboot3_modle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwdui.springboot3_modle.mapper.UserMapper;
import com.wwdui.springboot3_modle.pojo.User;
import com.wwdui.springboot3_modle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
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
}
