package com.qt.bus.jwt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserLoginInfo {

    String nickName;
    String userType;
    Long userId;
    Long teamId;
}