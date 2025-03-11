package com.wwdui.springboot3_modle.controller;

import com.wwdui.springboot3_modle.pojo.GoodRequest;
import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @PostMapping("/buy")
    public Result<?> buy(@RequestBody GoodRequest goodRequest){
        try {
            return goodService.buyGoodById(goodRequest.getGoodId(),goodRequest.getBuyNum());
        }catch (RuntimeException e){
            return Result.error(400, e.getMessage());
        }

    }
}
