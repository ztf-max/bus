package com.qt.bus.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qt.bus.dao.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 乘客表Mapper
 */
@Mapper
public interface UsersMapper extends BaseMapper<User> {

}
