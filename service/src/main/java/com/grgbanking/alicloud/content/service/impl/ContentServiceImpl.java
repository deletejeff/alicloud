package com.grgbanking.alicloud.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.grgbanking.alicloud.common.entity.content.ContentEntity;
import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;
import com.grgbanking.alicloud.common.po.content.ContentPo;
import com.grgbanking.alicloud.content.service.ContentService;
import com.grgbanking.alicloud.dao.content.ContentDao;
import com.grgbanking.alicloud.dao.mq.RocketTransactionLogDao;
import com.grgbanking.alicloud.userclient.feignclient.UserFeignClient;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author machao
 */
@Service
public class ContentServiceImpl implements ContentService {
    public static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RocketTransactionLogDao rocketTransactionLogDao;

//    @Autowired
//    private RestTemplate restTemplate;

    /**
     * getByContentId
     *
     * @param contentId
     * @return
     */
    @Override
    public ContentPo getByContentId(String contentId) {
        ContentEntity contentEntity = contentDao.selectById(contentId);
        ContentPo contentPo = new ContentPo();
        BeanUtils.copyProperties(contentEntity, contentPo);
//        UserEntity userEntity = restTemplate.getForObject(
//                "http://localhost:8080/ali-demo/user/getUserByKey",
//                UserEntity.class);
//        contentPo.setUsername(userEntity.getUsername());
        UserEntity userEntity = userFeignClient.getUserByKey(contentPo.getUserid());
        contentPo.setUsername(userEntity.getUsername());
        return contentPo;
    }

    /**
     * 添加内容数据,并添加积分通过feignClient
     * @param contentEntity
     * @return
     */
    @Override
    public boolean addContentByFeignClient(ContentEntity contentEntity){
        int res = contentDao.insert(contentEntity);
        /**
         * 第一种方式：
         * 直接通过调用feignClient为用户添加积分
         */
        userFeignClient.addUserPoints();
        return res > 0;
    }

    /**
     * 添加内容数据，并通过rocketMq添加积分
     *
     * @param contentEntity
     * @return
     */
    @Override
    public boolean addContentByMq(ContentEntity contentEntity) {
        int res = contentDao.insert(contentEntity);
        /**
         * 第二种方式：
         * 发送消息给rocketmq，让用户中心去消费rocketmq的信息，为用户添加积分
         */
        UserAddPointsMqMsg userAddPointsMqMsg = new UserAddPointsMqMsg();
        userAddPointsMqMsg.setUserid(contentEntity.getUserid());
        userAddPointsMqMsg.setPoints(50);
        userAddPointsMqMsg.setDescription("通过mq消息，评论获取积分");
        userAddPointsMqMsg.setEvent("ADD");
        rocketMQTemplate.convertAndSend("add-points", userAddPointsMqMsg);
        return res > 0;
    }

    /**
     * 添加内容数据,通过rocketMq,增加事务
     *
     * @param contentEntity
     * @return
     */
    @Override
    public boolean addContentByMqTransaction(ContentEntity contentEntity){
        try {
            String transactionId = UUID.randomUUID().toString();
            logger.info("生成的 transactionId = {}", transactionId);
            UserAddPointsMqMsg userAddPointsMqMsg = new UserAddPointsMqMsg();
            userAddPointsMqMsg.setUserid(contentEntity.getUserid());
            userAddPointsMqMsg.setPoints(50);
            userAddPointsMqMsg.setDescription("通过mqTransaction消息，评论获取积分");
            userAddPointsMqMsg.setEvent("ADD");
            //发送半消息(半消息：在事务中发送但消费者不使用的消息，等事务状态确定再使用或放弃的消息)
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(
                    "tx-add-points-group",
                    "add-points-transaction",
                    MessageBuilder
                            .withPayload(userAddPointsMqMsg)
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader("contentId", contentEntity.getContentId())
                            .setHeader("dto", JSON.toJSONString(userAddPointsMqMsg))
                            .build(),
                    contentEntity
            );
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加内容数据,并添加事务日志
     * @param contentEntity
     * @param transactionId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addContentWithTransactionLog(ContentEntity contentEntity,String transactionId){
        int insertContent = contentDao.insert(contentEntity);
        RocketTransactionLog rocketTransactionLog = new RocketTransactionLog();
        rocketTransactionLog.setId(transactionId);
        rocketTransactionLog.setTransactionId(transactionId);
        rocketTransactionLog.setStatus(insertContent);
        rocketTransactionLog.setLog(insertContent>0 ? "success" : "failure");
        rocketTransactionLogDao.insert(rocketTransactionLog);
        logger.info("添加内容成功，用户：{}，事务id：{}，内容：{}", contentEntity.getUserid(), transactionId, contentEntity.getContent());
    }
}
