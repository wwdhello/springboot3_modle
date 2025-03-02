package com.wwdui.springboot3_modle.service;

import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.pojo.User;



public interface UserService {
    User registerUser(User user);

    Result<?> login(User user);

    Result<?> logout();
}
