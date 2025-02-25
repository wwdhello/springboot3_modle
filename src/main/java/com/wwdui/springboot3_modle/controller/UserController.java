package com.wwdui.springboot3_modle.controller;

import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.User;
import com.wwdui.springboot3_modle.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
