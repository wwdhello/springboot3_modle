package com.wwdui.springboot3_modle.controller;

import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.User;
import com.wwdui.springboot3_modle.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user){
        try {
            User registeredUser = userService.registerUser(user);
            return Result.success(registeredUser);
        }catch (RuntimeException e){
            return Result.error(400,e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody User user){
        try {
            return userService.login(user);
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }

    }

    @PostMapping("/logout")
    public Result<?> logout(){
        try {
            return userService.logout();
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/details")
    public Result<?> getUser(){
        try {
            return userService.getUser();
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }
    }



}
