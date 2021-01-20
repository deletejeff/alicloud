package com.grgbanking.alicloud.user.stream.consumer;

import com.grgbanking.alicloud.common.stream.MessageChannelName;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @author machao
 */
@Component
public interface UserMessageChannelConsumer {

    /**
     * 添加积分的消息管道，需要在yml中进行配置
     * @return
     */
    @Input(MessageChannelName.MY_STREAM_ADD_POINTS)
    SubscribableChannel addPointsInPut();
}
