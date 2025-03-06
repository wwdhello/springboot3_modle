package com.wwdui.springboot3_modle.service;

import com.github.pagehelper.PageInfo;
import com.wwdui.springboot3_modle.pojo.FileEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String checkFileExists(String md5);

    boolean uploadChunk(MultipartFile multipartFile, String md5, int chunkIndex, int totalChunks);

    PageInfo<FileEntity> listFilePathsByUserId( Integer pageNum, Integer pageSize);
}
