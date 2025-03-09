package com.wwdui.springboot3_modle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwdui.springboot3_modle.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}
