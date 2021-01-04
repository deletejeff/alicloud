package com.grgbanking.alicloud.mq.service;

import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;

/**
 * @author machao
 */
public interface RocketTransactionLogService {
    /**
     * 插入rocketmq日志
     * @param rocketTransactionLog
     * @return
     */
    boolean insertLog(RocketTransactionLog rocketTransactionLog);
}
