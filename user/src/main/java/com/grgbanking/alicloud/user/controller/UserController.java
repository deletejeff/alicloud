package com.grgbanking.alicloud.user.controller;

import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author machao
 */
@RestController
@RequestMapping(value = "/user",method = RequestMethod.POST)
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getUserByKey/{userid}")
    public UserEntity getUserByKey(@PathVariable String userid){
        logger.info("请求：getUserByKey");
        return userService.getUserByKey(userid);
    }

    @GetMapping("/addUser")
    public List<UserEntity> addUser(){
        UserEntity userEntity = new UserEntity();
        Random random = new Random();
        int nextInt = random.nextInt();
        userEntity.setUserid("machao" + nextInt);
        userEntity.setUsername("马超" + nextInt);
        boolean res = userService.addUser(userEntity);
        if(res){
            return userService.getUserAll();
        }
        return null;
    }

    @GetMapping("/addUserPoints")
    public Map<String,Object> addUserPoints(){
        Map<String, Object> map = new HashMap<>(2);
        try {
            boolean res = userService.addUserPoints("machao", 1);
            if(res){
                map.put("errorCode", 0);
                map.put("errorMsg", "success");
            }else{
                map.put("errorCode", -1);
                map.put("errorMsg", "failure");
            }
        } catch (Exception e) {
            map.put("errorCode", -1);
            map.put("errorMsg", "user points update failure!");
            logger.error("user points update failure!", e);
        }
        return map;
    }
}
