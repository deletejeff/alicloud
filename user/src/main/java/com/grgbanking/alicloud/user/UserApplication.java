package com.grgbanking.alicloud.user;

import com.grgbanking.alicloud.user.stream.consumer.UserMessageChannelConsumer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author machao
 * Sink.class对应的是spring cloud stream默认的input通道（对应接收的管道为output，类为：Source.class）
 */
@MapperScan("com.grgbanking.alicloud.dao")
@ComponentScan("com.grgbanking.alicloud")
@EnableFeignClients(basePackages = "com.grgbanking.alicloud")
@EnableBinding({Sink.class, UserMessageChannelConsumer.class})
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
