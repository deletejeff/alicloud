package com.grgbanking.alicloud.content.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.grgbanking.alicloud.common.entity.content.ContentEntity;
import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;
import com.grgbanking.alicloud.content.service.ContentService;
import com.grgbanking.alicloud.dao.content.ContentDao;
import com.grgbanking.alicloud.dao.mq.RocketTransactionLogDao;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * 处理rocketmq半消息的监听类
 *
 * @author machao
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-points-group")
public class AddPointsMqTransactionListener implements RocketMQLocalTransactionListener {
    public static final Logger logger = LoggerFactory.getLogger(AddPointsMqTransactionListener.class);
    @Autowired
    private RocketTransactionLogDao rocketTransactionLogDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private ContentService contentService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        RocketMQLocalTransactionState state = null;
        try {
            ContentEntity contentEntity = JSONObject.parseObject((String) msg.getHeaders().get("contentEntity"),ContentEntity.class);
            String transactionId = msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID, String.class);
            contentService.addContentWithTransactionLog(contentEntity,transactionId);
            state = RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            logger.error("添加内容数据,并添加事务日志异常", e);
            state = RocketMQLocalTransactionState.ROLLBACK;
        }
        logger.info("execute 方法状态返回 : {}", state);
        return state;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        RocketMQLocalTransactionState state = null;
        try {
            String transactionId = msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID, String.class);
            logger.info("进入rocketmq回查commit状态方法，transactionId = {}", transactionId);
            QueryWrapper<RocketTransactionLog> queryWrapper = new QueryWrapper<RocketTransactionLog>().eq("transaction_id", transactionId);
            RocketTransactionLog rocketTransactionLog = rocketTransactionLogDao.selectOne(queryWrapper);
            if(rocketTransactionLog == null){
                state = RocketMQLocalTransactionState.UNKNOWN;
            }else{
                state = rocketTransactionLog.getStatus() == 1 ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            logger.error("用户积分入库事务失败，回滚！",e);
            state = RocketMQLocalTransactionState.ROLLBACK;
        }
        logger.info("回查commit状态方法，返回 : {}", state);
        return state;
    }

}
