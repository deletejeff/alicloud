package com.grgbanking.alicloud.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.grgbanking.alicloud.common.entity.user.UserEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author machao
 */
public interface UserDao extends BaseMapper<UserEntity> {
    /**
     * 添加/减少积分
     * @param userid
     * @param points
     * @return
     */
    int addUserPoints(@Param("userid") String userid, @Param("points") int points);
}
