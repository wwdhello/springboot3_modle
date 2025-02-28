package com.wwdui.springboot3_modle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wwdui.springboot3_modle.mapper")
public class Springboot3ModleApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot3ModleApplication.class, args);
    }

}
