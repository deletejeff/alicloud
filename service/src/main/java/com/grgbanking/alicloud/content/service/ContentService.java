package com.grgbanking.alicloud.content.service;


import com.grgbanking.alicloud.common.entity.content.ContentEntity;
import com.grgbanking.alicloud.common.po.content.ContentPo;

/**
 * @author machao
 */
public interface ContentService {
    /**
     * getByContentId
     * @param contentId
     * @return
     */
    public ContentPo getByContentId(String contentId);

    /**
     * 添加内容数据,并添加积分通过feignClient
     * @param contentEntity
     * @return
     */
    public boolean addContentByFeignClient(ContentEntity contentEntity);

    /**
     * 添加内容数据，并通过rocketMq添加积分
     * @param contentEntity
     * @return
     */
    public boolean addContentByMq(ContentEntity contentEntity);

    /**
     * 添加内容数据,通过rocketMq添加积分,增加事务
     * @param contentEntity
     * @return
     */
    public boolean addContentByMqTransaction(ContentEntity contentEntity);

    /**
     * 添加内容数据,并添加事务日志
     *
     * @param contentEntity
     * @param transactionId
     * @return
     */
    public void addContentWithTransactionLog(ContentEntity contentEntity, String transactionId);
}
