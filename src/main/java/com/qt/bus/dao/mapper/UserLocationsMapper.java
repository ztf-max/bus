package com.qt.bus.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qt.bus.dao.model.UserLocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 乘客实时位置表Mapper
 */
@Mapper
public interface UserLocationsMapper extends BaseMapper<UserLocation> {

}
