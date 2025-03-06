package com.wwdui.springboot3_modle.pojo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageRequest {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum;

    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小最小为1")
    private Integer pageSize;
}
