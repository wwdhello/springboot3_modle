package com.wwdui.springboot3_modle.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "file")
public class FileEntity {
    @TableId(type = IdType.AUTO)
    private Long fid;
    @NotBlank
    private String md5;
    @NotBlank
    private String filePath;
    @NotBlank
    private Long userId; // 关联用户ID
    @NotBlank
    private Date createdTime;
}
