package com.wwdui.springboot3_modle.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VerifyCodeRequest {
    @NotBlank
    private String email;

    private String code;
}
