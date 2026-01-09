package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.UsersMapper;
import com.qt.bus.dao.model.User;
import org.springframework.stereotype.Repository;

/**
 * 乘客表Repository
 */
@Repository
public class UserRepository extends ServiceImpl<UsersMapper, User> {

}
