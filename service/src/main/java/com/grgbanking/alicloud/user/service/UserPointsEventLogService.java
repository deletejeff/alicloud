package com.grgbanking.alicloud.user.service;

import com.grgbanking.alicloud.common.entity.user.UserPointsEventLogEntity;

/**
 * @author machao
 */
public interface UserPointsEventLogService {
    /**
     * 添加基本变动明细
     * @param userPointsEventLogEntity
     * @return
     */
    boolean insertPointsLog(UserPointsEventLogEntity userPointsEventLogEntity);
}
