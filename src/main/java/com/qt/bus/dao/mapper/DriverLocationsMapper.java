package com.qt.bus.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qt.bus.dao.model.DriverLocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 司机实时位置表Mapper
 */
@Mapper
public interface DriverLocationsMapper extends BaseMapper<DriverLocation> {

}
