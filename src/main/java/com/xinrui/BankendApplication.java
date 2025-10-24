package com.xinrui;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
@MapperScan("com.xinrui.mapper")
public class BankendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankendApplication.class, args);
    }
}
