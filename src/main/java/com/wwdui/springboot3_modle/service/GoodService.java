package com.wwdui.springboot3_modle.service;

import com.wwdui.springboot3_modle.pojo.Result;

public interface GoodService {


    Result<?> buyGoodById(long goodId, int buyNum);
}
