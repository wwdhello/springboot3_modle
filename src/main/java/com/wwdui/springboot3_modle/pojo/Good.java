package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "good")//数据库映射,对应表名
public class Good {
    @TableId
    private Long goodId;

    private String goodName;

    private int goodNum;
}
