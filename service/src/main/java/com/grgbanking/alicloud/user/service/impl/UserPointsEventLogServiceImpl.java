package com.grgbanking.alicloud.user.service.impl;

import com.grgbanking.alicloud.common.entity.user.UserPointsEventLogEntity;
import com.grgbanking.alicloud.dao.user.UserPointsEventLogDao;
import com.grgbanking.alicloud.user.service.UserPointsEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author machao
 */
@Service
public class UserPointsEventLogServiceImpl implements UserPointsEventLogService {
    @Autowired
    private UserPointsEventLogDao userPointsEventLogDao;
    /**
     * 添加基本变动明细
     *
     * @param userPointsEventLogEntity
     * @return
     */
    @Override
    public boolean insertPointsLog(UserPointsEventLogEntity userPointsEventLogEntity) {
        return userPointsEventLogDao.insert(userPointsEventLogEntity) > 0;
    }
}
