package com.wwdui.springboot3_modle.service.impl;

import com.wwdui.springboot3_modle.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
//生成验证码并存储到Redis
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String generateAndStoreCode(String email) {
        String code = String.valueOf((int) (Math.random() * 900000) + 100000); // 生成6位验证码
        redisTemplate.opsForValue().set(email, code, 3, TimeUnit.MINUTES); // 存储到Redis，5分钟过期
        return code;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return code != null && code.equals(storedCode);
    }

    @Override
    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("wwdui@foxmail.com");
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("验证码为: " + code);
        mailSender.send(message);
    }
}
