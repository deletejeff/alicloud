package com.grgbanking.alicloud.userclient.feignclient;

import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.userclient.feignclient.fallbackfactory.UserFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 服务发现接口
 * @author machao
 */
//@FeignClient(name = "user" ,configuration = UserFeignClientConfiguration.class)
@FeignClient(name = "user", fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {
    /**
     * 通过userid获取用户信息
     * @param userid
     * @return
     */
    @GetMapping("/user/user/getUserByKey/{userid}")
    public UserEntity getUserByKey(@PathVariable String userid);

    /**
     * 给用户增加/减少积分
     *
     * @return
     */
    @GetMapping("/user/user/addUserPoints")
    public Map<String, Object> addUserPoints();
}
