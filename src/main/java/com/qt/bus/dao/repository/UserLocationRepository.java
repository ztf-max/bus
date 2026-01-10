package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.UserLocationsMapper;
import com.qt.bus.dao.model.UserLocation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户实时位置表Repository（包含乘客和司机）
 */
@Repository
public class UserLocationRepository extends ServiceImpl<UserLocationsMapper, UserLocation> {

    /**
     * 根据用户ID查询位置信息
     *
     * @param userId 用户ID
     * @return 用户位置，不存在则返回null
     */
    public UserLocation getByUserId(Long userId) {
        LambdaQueryWrapper<UserLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLocation::getUserId, userId);
        return this.getOne(queryWrapper);
    }

    /**
     * 查询所有用户位置（在线状态）
     *
     * @return 用户位置列表
     */
    public List<UserLocation> listAllLocations() {
        LambdaQueryWrapper<UserLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserLocation::getGmtModified);
        return this.list(queryWrapper);
    }

    /**
     * 根据用户ID列表批量查询位置
     *
     * @param userIds 用户ID列表
     * @return 位置列表
     */
    public List<UserLocation> listByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<UserLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserLocation::getUserId, userIds);
        return this.list(queryWrapper);
    }
}
