package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@TableName(value = "good")//数据库映射,对应表名
@Document(indexName = "good")
public class Good {
    @TableId// MyBatis Plus 注解，标记数据库主键
    @Id// Spring Data Elasticsearch 注解，标记 Elasticsearch 文档主键
    private Long goodId;

    @Field(type = FieldType.Text)
    private String goodName;

    @Field(type = FieldType.Integer)
    private int goodNum;
}
