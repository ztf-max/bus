package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.DriverLocationsMapper;
import com.qt.bus.dao.model.DriverLocation;
import org.springframework.stereotype.Repository;

/**
 * 司机实时位置表Repository
 */
@Repository
public class DriverLocationRepository extends ServiceImpl<DriverLocationsMapper, DriverLocation> {

}
