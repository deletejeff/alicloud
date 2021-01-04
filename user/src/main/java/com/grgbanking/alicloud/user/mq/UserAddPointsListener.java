package com.grgbanking.alicloud.user.mq;

import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.user.service.UserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 处理mq中的消息
 * @author machao
 */
@Service
@RocketMQMessageListener(consumerGroup = "user-consumer-group", topic = "add-points")
public class UserAddPointsListener implements RocketMQListener<UserAddPointsMqMsg> {
    @Autowired
    private UserService userService;
    @Override
    public void onMessage(UserAddPointsMqMsg message) {
        //添加积分
        userService.addUserPoints(message.getUserid(), message.getPoints());
    }
}
