package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.UserLocationsMapper;
import com.qt.bus.dao.model.UserLocation;
import org.springframework.stereotype.Repository;

/**
 * 乘客实时位置表Repository
 */
@Repository
public class UserLocationRepository extends ServiceImpl<UserLocationsMapper, UserLocation> {

}
