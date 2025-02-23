package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")//数据库映射,对应表名
public class User {
    private Long id;          // 用户ID
    private String username;  // 账号
    private String password;  // 密码
    private String name;      // 姓名
}
