package com.wwdui.springboot3_modle.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {
    @NotBlank
    private String md5;

    private MultipartFile multipartFile;

    private int chunkIndex;

    private int totalChunks;

}
