package com.grgbanking.alicloud.content.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author machao
 * feign的配置类
 * 这个类别加@Configuration注解，否则必须挪到@ComponentScan能扫描的包以外
 */
public class UserFeignClientConfiguration {

    /**
     * feign的日志级别配置
     * @return
     */
    @Bean
    public Logger.Level feignLevel(){
        return Logger.Level.BASIC;
    }
}
