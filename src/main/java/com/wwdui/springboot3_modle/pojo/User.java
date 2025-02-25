package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@TableName("user")//数据库映射,对应表名
public class User {
    private Long id;          // 用户ID
    @NotBlank(message = "不能为空")
    @Size(min = 5,max = 15,message = "长度为5-15")
    private String username;  // 账号

    @NotBlank(message = "不能为空")
    @Size(min = 5,max = 20,message = "长度为5-20")
    private String password;  // 密码


    private String name;      // 姓名
}
