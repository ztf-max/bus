package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.DriversMapper;
import com.qt.bus.dao.model.Driver;
import org.springframework.stereotype.Repository;

/**
 * 司机表Repository
 */
@Repository
public class DriverRepository extends ServiceImpl<DriversMapper, Driver> {

}
