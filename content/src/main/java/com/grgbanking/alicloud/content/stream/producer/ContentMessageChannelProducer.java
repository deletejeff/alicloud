package com.grgbanking.alicloud.content.stream.producer;

import com.grgbanking.alicloud.common.stream.MessageChannelName;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author machao
 */
@Component
public interface ContentMessageChannelProducer {

    /**
     * 添加积分的消息管道，需要在yml中进行配置
     * @return
     */
    @Output(MessageChannelName.MY_STREAM_ADD_POINTS)
    MessageChannel addPointsOutPut();
}
