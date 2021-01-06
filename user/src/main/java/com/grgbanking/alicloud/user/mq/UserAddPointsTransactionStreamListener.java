package com.grgbanking.alicloud.user.mq;

import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;


/**
 * 处理mq中的消息
 * @author machao
 */
@Service
public class UserAddPointsTransactionStreamListener {
    @Autowired
    private UserService userService;

    @StreamListener(value = Sink.INPUT, condition = "headers['filterHeader']=='just do it'")
    public void onMessage(UserAddPointsMqMsg message) {
        //添加积分
        userService.addUserPointsByTransaction(message);
    }
}
