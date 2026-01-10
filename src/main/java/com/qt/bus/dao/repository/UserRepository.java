package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.UserMapper;
import com.qt.bus.dao.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表Repository
 */
@Repository
public class UserRepository extends ServiceImpl<UserMapper, User> {

    /**
     * 用户类型：乘客
     */
    public static final String USER_TYPE_USER = "user";

    /**
     * 用户类型：司机
     */
    public static final String USER_TYPE_DRIVER = "driver";

    /**
     * 根据OpenID查询用户
     *
     * @param openid 微信OpenID
     * @return 用户信息，不存在则返回null
     */
    public User getByOpenid(String openid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据用户类型查询所有用户
     *
     * @param userType 用户类型（user/driver）
     * @return 用户列表
     */
    public List<User> listByUserType(String userType) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserType, userType);
        return this.list(queryWrapper);
    }

    /**
     * 查询所有司机
     *
     * @return 司机列表
     */
    public List<User> listAllDrivers() {
        return listByUserType(USER_TYPE_DRIVER);
    }

    /**
     * 查询所有乘客
     *
     * @return 乘客列表
     */
    public List<User> listAllUsers() {
        return listByUserType(USER_TYPE_USER);
    }
}
