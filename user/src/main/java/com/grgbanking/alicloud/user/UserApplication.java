package com.grgbanking.alicloud.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author machao
 */
@MapperScan("com.grgbanking.alicloud.dao")
@ComponentScan("com.grgbanking.alicloud")
@EnableFeignClients(basePackages = "com.grgbanking.alicloud")
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}