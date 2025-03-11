package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user_good")
public class UserGood {
    private Long id;
    private Long userId;
    private Long goodId;
    private Integer buyNum;
}
