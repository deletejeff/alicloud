package com.grgbanking.alicloud.user.mq;

import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.common.stream.MessageChannelName;
import com.grgbanking.alicloud.user.auth.CheckLogin;
import com.grgbanking.alicloud.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;


/**
 * 处理mq中的消息
 * 通过spring cloud stream方式调用的，ContentController.addContentByCloudStream
 * @author machao
 */
@Service
public class UserAddPointsTransactionStreamListener {
    public static final Logger logger = LoggerFactory.getLogger(UserAddPointsTransactionStreamListener.class);
    @Autowired
    private UserService userService;

    @CheckLogin
    @StreamListener(value = Sink.INPUT, condition = "headers['filterHeader']=='just do it'")
    public void onMessage(UserAddPointsMqMsg message) {
        //添加积分
        userService.addUserPointsByTransaction(message);
        logger.info("添加积分成功，用户：{}，增加积分：{}", message.getUserid(), message.getPoints());
    }

    @StreamListener(value = MessageChannelName.MY_STREAM_ADD_POINTS, condition = "headers['filterHeader']=='just do it'")
    public void onMessage2(UserAddPointsMqMsg message) {
        //添加积分
        userService.addUserPointsByTransaction(message);
        logger.info("添加积分成功，用户：{}，增加积分：{}", message.getUserid(), message.getPoints());
    }
}
