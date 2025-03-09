package com.wwdui.springboot3_modle.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('nor')")
    public String test(){
        return "test";
    }
}
