package com.grgbanking.alicloud.user.service;


import com.grgbanking.alicloud.common.entity.user.UserEntity;
import com.grgbanking.alicloud.common.mq.UserAddPointsMqMsg;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author machao
 */
public interface UserService {
    /**
     * 获取用户
     * @param key
     * @return
     */
    UserEntity getUserByKey(String key);

    /**
     * insert
     * @param userEntity
     * @return
     */
    boolean addUser(UserEntity userEntity);

    /**
     * 获取所有用户
     * @return
     */
    List<UserEntity> getUserAll();

    /**
     * 给用户增加/减少积分
     * @param userid
     * @param points
     * @return
     */
    boolean addUserPoints(String userid,int points);

    /**
     * 给用户增加/减少积分,使用事务
     * @param userAddPointsMqMsg
     * @return
     */
    public boolean addUserPointsByTransaction(UserAddPointsMqMsg userAddPointsMqMsg);
}
