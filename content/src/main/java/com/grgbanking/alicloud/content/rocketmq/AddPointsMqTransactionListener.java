package com.grgbanking.alicloud.content.rocketmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.grgbanking.alicloud.common.entity.content.ContentEntity;
import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;
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
@RocketMQTransactionListener
public class AddPointsMqTransactionListener implements RocketMQLocalTransactionListener {
    public static final Logger logger = LoggerFactory.getLogger(AddPointsMqTransactionListener.class);
    @Autowired
    private RocketTransactionLogDao rocketTransactionLogDao;
    @Autowired
    private ContentDao contentDao;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        RocketMQLocalTransactionState commit = null;
        boolean res = false;
        try {
            ContentEntity contentEntity = (ContentEntity) arg;
            contentEntity.setRemark("积分发送到用户微服务，等待处理");
            res = contentDao.updateById(contentEntity) > 0;
            commit = getRocketTransactionState(msg);
            updatePointsResult(msg, commit);
        } catch (Exception e) {
            commit = RocketMQLocalTransactionState.ROLLBACK;
            logger.error("用户积分入库事务失败，回滚！",e);
        }
        return commit;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        RocketMQLocalTransactionState commit = null;
        try {
            commit = getRocketTransactionState(msg);
        } catch (Exception e) {
            logger.error("用户积分入库事务失败，回滚！",e);
            commit = RocketMQLocalTransactionState.ROLLBACK;
        }
        updatePointsResult(msg, commit);
        return commit;
    }

    private void updatePointsResult(Message msg, RocketMQLocalTransactionState commit) {
        try {
            String contentId = msg.getHeaders().get("contentId", String.class);
            ContentEntity contentEntity = contentDao.selectById(contentId);
            if (commit == RocketMQLocalTransactionState.COMMIT) {
                contentEntity.setRemark("积分添加成功");
                contentDao.updateById(contentEntity);
            } else if (commit == RocketMQLocalTransactionState.ROLLBACK) {
                contentEntity.setRemark("积分添加失败");
                contentDao.updateById(contentEntity);
            } else if(commit == RocketMQLocalTransactionState.UNKNOWN){
                //未知状态不处理
            }
        } catch (Exception e) {
            logger.error("remark更新失败", e);
        }
    }

    private RocketMQLocalTransactionState getRocketTransactionState(Message msg){
        String transactionId = msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID, String.class);
        logger.info("查询到的事务transactionId = {}", transactionId);
        QueryWrapper<RocketTransactionLog> queryWrapper = new QueryWrapper<RocketTransactionLog>().eq("transaction_id", transactionId);
        RocketTransactionLog rocketTransactionLog = rocketTransactionLogDao.selectOne(queryWrapper);
        if(rocketTransactionLog == null){
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return rocketTransactionLog.getStatus() == 1 ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }
}
