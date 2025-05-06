package com.wwdui.springboot3_modle.controller;

import com.wwdui.springboot3_modle.pojo.Good;
import com.wwdui.springboot3_modle.pojo.GoodRequest;
import com.wwdui.springboot3_modle.pojo.Result;
import com.wwdui.springboot3_modle.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("search")
    public Result<List<Good>> search(@RequestParam String query){
            return Result.success(goodService.searchProducts(query));
    }
}
