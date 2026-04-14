package com.opchub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.opchub.mapper")
public class OpcHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpcHubApplication.class, args);
    }
}