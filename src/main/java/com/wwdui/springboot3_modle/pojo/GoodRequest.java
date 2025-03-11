package com.wwdui.springboot3_modle.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GoodRequest {
    @NotNull
    private long goodId;

    @NotNull
    private int buyNum;
}
