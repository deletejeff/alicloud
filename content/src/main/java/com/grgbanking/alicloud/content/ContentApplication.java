package com.grgbanking.alicloud.content;

import com.grgbanking.alicloud.content.stream.producer.ContentMessageChannelProducer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @author machao
 * Source.class对应的是spring cloud stream默认的output通道（对应接收的管道为input，类为：Sink.class）
 */
@MapperScan("com.grgbanking.alicloud.dao")
@ComponentScan("com.grgbanking.alicloud")
@EnableFeignClients(basePackages = "com.grgbanking.alicloud")
@EnableBinding({Source.class, ContentMessageChannelProducer.class})
@SpringBootApplication
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
