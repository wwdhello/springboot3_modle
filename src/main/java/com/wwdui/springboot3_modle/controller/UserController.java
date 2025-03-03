package com.wwdui.springboot3_modle.controller;

import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.User;
import com.wwdui.springboot3_modle.pojo.VerifyCodeRequest;
import com.wwdui.springboot3_modle.service.UserService;
import com.wwdui.springboot3_modle.service.VerificationCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user){
        try {
            User registeredUser = userService.registerUser(user);
            return Result.success(registeredUser);
        }catch (RuntimeException e){
            return Result.error(400,e.getMessage());
        }
    }

    //登录
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody User user){
        try {
            return userService.login(user);
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }

    }

    //登出
    @PostMapping("/logout")
    public Result<?> logout(){
        try {
            return userService.logout();
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }
    }

    //信息
    @GetMapping("/details")
    public Result<?> getUser(){
        try {
            return userService.getUser();
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/sendcode")
    public Result<?> sendCode(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest){
        try {
            String code = verificationCodeService.generateAndStoreCode(verifyCodeRequest.getEmail());
            verificationCodeService.sendVerificationCode(verifyCodeRequest.getEmail(),code);
            return Result.success("验证码已发送");
        }catch (RuntimeException e){
            return Result.error(400,e.getMessage());
        }
    }

    @PostMapping("/verifycode")
    public Result<?> verifyCode(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest){
        boolean isValid = verificationCodeService.verifyCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
        try {
            if (isValid){
                return Result.success("验证成功");
            }else {
                return Result.error(400,"验证码错误");
            }
        }catch (RuntimeException e){
            return Result.error(400,e.getMessage());
        }
    }



}
