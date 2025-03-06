package com.wwdui.springboot3_modle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.wwdui.springboot3_modle.pojo.FileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
    Page<FileEntity> selectFilePathsByUserId(@Param("userId")Long userId);
}
