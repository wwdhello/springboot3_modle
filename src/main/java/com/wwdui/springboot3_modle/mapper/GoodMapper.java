package com.wwdui.springboot3_modle.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwdui.springboot3_modle.pojo.Good;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodMapper extends BaseMapper<Good> {
}
