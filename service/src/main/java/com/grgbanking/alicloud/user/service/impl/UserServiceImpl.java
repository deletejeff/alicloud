package com.grgbanking.alicloud.user.service.impl;

import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.common.entity.user.UserPointsEventLogEntity;
import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import com.grgbanking.alicloud.common.mq.entity.RocketTransactionLog;
import com.grgbanking.alicloud.dao.mq.RocketTransactionLogDao;
import com.grgbanking.alicloud.dao.user.UserDao;
import com.grgbanking.alicloud.dao.user.UserPointsEventLogDao;
import com.grgbanking.alicloud.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author machao
 */
@Service
public class UserServiceImpl implements UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private RocketTransactionLogDao rocketTransactionLogDao;
    @Autowired
    private UserPointsEventLogDao userPointsEventLogDao;
    /**
     * 获取用户
     * @param key
     * @return
     */
    @Override
    public UserEntity getUserByKey(String key){
        return userDao.selectById(key);
    }

    /**
     * insert
     *
     * @param userEntity
     * @return
     */
    @Override
    public boolean addUser(UserEntity userEntity) {
        return userDao.insert(userEntity) > 0;
    }

    @Override
    public List<UserEntity> getUserAll() {
        return userDao.selectList(null);
    }

    /**
     * 给用户增加/减少积分
     *
     * @param userid
     * @param points
     * @return
     */
    @Override
    public boolean addUserPoints(String userid, int points) {
            return userDao.addUserPoints(userid, points) > 0;
    }

    /**
     * 给用户增加/减少积分,使用事务
     * @param userAddPointsMqMsg
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUserPointsByTransaction(UserAddPointsMqMsg userAddPointsMqMsg){
        try {
            int res = userDao.addUserPoints(userAddPointsMqMsg.getUserid(), userAddPointsMqMsg.getPoints());
            //添加积分明细
            UserPointsEventLogEntity userPointsEventLogEntity = new UserPointsEventLogEntity();
            userPointsEventLogEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            userPointsEventLogEntity.setUserid(userAddPointsMqMsg.getUserid());
            userPointsEventLogEntity.setPoints(userAddPointsMqMsg.getPoints());
            userPointsEventLogEntity.setEvent(userAddPointsMqMsg.getEvent());
            userPointsEventLogEntity.setCreateTime(new Date());
            userPointsEventLogEntity.setDescription(userAddPointsMqMsg.getDescription());
            userPointsEventLogDao.insert(userPointsEventLogEntity);
            return res > 0;
        } catch (Exception e) {
            logger.error("添加积分失败", e);
            return false;
        }
    }
}
