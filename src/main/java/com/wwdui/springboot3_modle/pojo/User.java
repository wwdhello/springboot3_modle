package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")//数据库映射,对应表名
public class User implements Serializable {

    private static final long serialVersionUID = -40356785423868312L;

    @TableId
    private Long id;// 用户ID

    @NotBlank(message = "不能为空")
    @Size(min = 5,max = 15,message = "长度为5-15")
    private String username;  // 账号

    @NotBlank(message = "不能为空")
    @Size(min = 5,max = 20,message = "长度为5-20")
    private String password;  // 密码


    private String name;      // 姓名
}
