package com.grgbanking.alicloud.mq.service.impl;

import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;
import com.grgbanking.alicloud.dao.mq.RocketTransactionLogDao;
import com.grgbanking.alicloud.mq.service.RocketTransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author machao
 */
@Service
public class RocketTransactionLogServiceImpl implements RocketTransactionLogService {

    @Autowired
    private RocketTransactionLogDao rocketTransactionLogDao;

    /**
     * 插入rocketmq日志
     *
     * @param rocketTransactionLog
     * @return
     */
    @Override
    public boolean insertLog(RocketTransactionLog rocketTransactionLog) {
        return rocketTransactionLogDao.insert(rocketTransactionLog) > 0;
    }
}
