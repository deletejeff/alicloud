package com.grgbanking.alicloud.user.controller;

import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.common.entity.user.UserPointsEventLogEntity;
import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.dao.user.UserDao;
import com.grgbanking.alicloud.dao.user.UserPointsEventLogDao;
import com.grgbanking.alicloud.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author machao
 */
@RestController
@RequestMapping(value = "/user",method = RequestMethod.POST)
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPointsEventLogDao userPointsEventLogDao;

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

    // condition是用来过滤消息的条件
    // headers['filterHeader']=='just do it' 表示 headers中参数filterHeader的值为just do it则执行
//    @StreamListener(value = Sink.INPUT,condition = "headers['filterHeader']=='just do it'")
//    public void addContentByCloudStream(UserAddPointsMqMsg userAddPointsMqMsg){
//        try {
//            int res = userDao.addUserPoints(userAddPointsMqMsg.getUserid(), userAddPointsMqMsg.getPoints());
//            //添加积分明细
//            UserPointsEventLogEntity userPointsEventLogEntity = new UserPointsEventLogEntity();
//            userPointsEventLogEntity.setId(UUID.randomUUID().toString().replace("-", ""));
//            userPointsEventLogEntity.setUserid(userAddPointsMqMsg.getUserid());
//            userPointsEventLogEntity.setPoints(userAddPointsMqMsg.getPoints());
//            userPointsEventLogEntity.setEvent(userAddPointsMqMsg.getEvent());
//            userPointsEventLogEntity.setCreateTime(new Date());
//            userPointsEventLogEntity.setDescription(userAddPointsMqMsg.getDescription());
//            userPointsEventLogDao.insert(userPointsEventLogEntity);
//        } catch (Exception e) {
//            logger.error("添加积分失败", e);
//        }
//    }

//    /**
//     * 全局异常处理
//     * @param message
//     */
//    @StreamListener("errorChannel")
//    public void error(Message<?> message){
//        ErrorMessage errorMessage = (ErrorMessage) message;
//        logger.error("steam调用发生异常了，errorMessage = {}" ,errorMessage);
//    }
}
