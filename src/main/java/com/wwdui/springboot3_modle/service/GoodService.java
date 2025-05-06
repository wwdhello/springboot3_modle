package com.wwdui.springboot3_modle.service;

import com.wwdui.springboot3_modle.pojo.Good;
import com.wwdui.springboot3_modle.pojo.Result;

import java.util.List;

public interface GoodService {


    Result<?> buyGoodById(long goodId, int buyNum);

    List<Good> searchProducts(String query);
}
