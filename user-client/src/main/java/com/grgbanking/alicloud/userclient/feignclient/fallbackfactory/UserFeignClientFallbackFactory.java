package com.grgbanking.alicloud.userclient.feignclient.fallbackfactory;

import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.userclient.feignclient.UserFeignClient;
//import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author machao
 */
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
    public static final Logger logger = LoggerFactory.getLogger(UserFeignClientFallbackFactory.class);
    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public UserEntity getUserByKey(String userid) {
                logger.error("远程调用被限流/降级了", cause);
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername("流控/降级返回的用户");
                return userEntity;
            }

            @Override
            public Map<String, Object> addUserPoints() {
                logger.error("远程调用被限流/降级了", cause);
                return null;
            }
        };
    }
}
