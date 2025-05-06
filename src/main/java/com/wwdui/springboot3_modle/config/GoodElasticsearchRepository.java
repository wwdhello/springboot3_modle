package com.wwdui.springboot3_modle.config;

import com.wwdui.springboot3_modle.pojo.Good;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GoodElasticsearchRepository extends ElasticsearchRepository<Good, Long> {
    List<Good> findByGoodName(String name);
}
