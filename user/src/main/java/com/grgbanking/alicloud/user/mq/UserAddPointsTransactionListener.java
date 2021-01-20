package com.grgbanking.alicloud.user.mq;

import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.user.service.UserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 处理mq中的消息
 * 通过rocketMQTemplate 带事务的方法 ContentController.addContentByMqTransaction 调用的
 * @author machao
 */
@Service
@RocketMQMessageListener(consumerGroup = "user-consumer-transaction-group", topic = "add-points-transaction")
public class UserAddPointsTransactionListener implements RocketMQListener<UserAddPointsMqMsg> {
    @Autowired
    private UserService userService;
    @Override
    public void onMessage(UserAddPointsMqMsg message) {
        //添加积分
        userService.addUserPointsByTransaction(message);
    }
}
