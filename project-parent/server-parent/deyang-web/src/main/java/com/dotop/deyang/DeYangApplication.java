package com.dotop.deyang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dotop.deyang.dc.mapper")
public class DeYangApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeYangApplication.class, args);
    }

}
