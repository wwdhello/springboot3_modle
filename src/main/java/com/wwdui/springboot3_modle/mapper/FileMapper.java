package com.wwdui.springboot3_modle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwdui.springboot3_modle.pojo.FileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
}
