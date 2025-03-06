package com.wwdui.springboot3_modle.controller;

import com.github.pagehelper.PageInfo;
import com.wwdui.springboot3_modle.pojo.FileEntity;
import com.wwdui.springboot3_modle.pojo.FileRequest;
import com.wwdui.springboot3_modle.pojo.PageRequest;
import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping ("/check")
    public Result<?> checkFileExists(@Valid @RequestBody FileRequest fileRequest){

        try {
            String filePath = fileService.checkFileExists(fileRequest.getMd5());
            if (filePath!=null){
                return Result.success(filePath);
            }else {
                return Result.error(400,"文件不存在");
            }
        }catch (RuntimeException e){
            return Result.error(404, e.getMessage());
        }
    }

    @PostMapping("/uploadChunk")
    public Result<?> uploadChunk(@Valid @RequestBody FileRequest fileRequest){
        try {
            boolean b=fileService.uploadChunk(fileRequest.getMultipartFile()
                    ,fileRequest.getMd5()
                    ,fileRequest.getChunkIndex()
                    ,fileRequest.getTotalChunks());
            if (b){
                return Result.success("分片上传成功");
            }else {
                return Result.error(400,"上传失败");
            }
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/list")
    public Result<?> listFilePathsByUserId(@Valid @RequestBody PageRequest pageRequest){
        try {
            PageInfo<FileEntity> pageInfo=fileService.listFilePathsByUserId(pageRequest.getPageNum(), pageRequest.getPageSize());
            return Result.success(pageInfo);
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }

    }

}
