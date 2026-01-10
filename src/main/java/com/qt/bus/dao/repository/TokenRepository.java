package com.qt.bus.dao.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qt.bus.dao.mapper.TokenMapper;
import com.qt.bus.dao.model.Token;
import org.springframework.stereotype.Repository;

/**
 * Token表Repository
 */
@Repository
public class TokenRepository extends ServiceImpl<TokenMapper, Token> {

    /**
     * 根据用户ID和客户端类型查询Token
     *
     * @param userId 用户ID
     * @param clientType 客户端类型（WEB/TEAM）
     * @return Token记录，不存在则返回null
     */
    public Token getByUserIdAndClientType(Long userId, String clientType) {
        LambdaQueryWrapper<Token> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Token::getUserId, userId)
                    .eq(Token::getClientType, clientType);
        return this.getOne(queryWrapper);
    }

    /**
     * 保存或更新Token
     * 根据唯一索引 uk_user_client(user_id, client_type) 自动判断
     *
     * @param token Token对象
     * @return 是否成功
     */
    public boolean saveOrUpdateToken(Token token) {
        Token existingToken = getByUserIdAndClientType(token.getUserId(), token.getClientType());
        if (existingToken != null) {
            token.setId(existingToken.getId());
            return this.updateById(token);
        } else {
            return this.save(token);
        }
    }
}
