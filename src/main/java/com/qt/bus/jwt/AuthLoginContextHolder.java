package com.qt.bus.jwt;

/**
 * 用于存储当前登录的用户信息
 */
public class AuthLoginContextHolder {

    private final static ThreadLocal<JwtUserLoginInfo> loginUserInfoContext = new ThreadLocal<>();

    public static void saveLoginUserInfo(JwtUserLoginInfo jwtUserLoginInfo) {
        loginUserInfoContext.set(jwtUserLoginInfo);
    }

    public static void clear() {
        loginUserInfoContext.remove();
    }

    public static JwtUserLoginInfo getLoginUserInfo() {
        return loginUserInfoContext.get();
    }

    public static String getLoginUserName() {
        JwtUserLoginInfo jwtUserLoginInfo = getLoginUserInfo();
        if (jwtUserLoginInfo == null || jwtUserLoginInfo.getNickName() == null) {
            return "";
        }
        return jwtUserLoginInfo.getNickName();
    }

    public static Long getLoginUserId() {
        JwtUserLoginInfo jwtUserLoginInfo = getLoginUserInfo();
        if (jwtUserLoginInfo == null || jwtUserLoginInfo.getUserId() == null) {
            return null;
        }
        return jwtUserLoginInfo.getUserId();
    }

    public static void setLoginUserId(Long userId) {
        JwtUserLoginInfo jwtUserLoginInfo = getLoginUserInfo();
        if (jwtUserLoginInfo == null || jwtUserLoginInfo.getUserId() == null) {
            saveLoginUserInfo(JwtUserLoginInfo.builder().userId(userId).build());
        }
    }



    public static Long getLoginTeamId() {
        JwtUserLoginInfo jwtUserLoginInfo = getLoginUserInfo();
        if (jwtUserLoginInfo == null || jwtUserLoginInfo.getTeamId() == null) {
            return null;
        }
        return jwtUserLoginInfo.getTeamId();
    }

    public static String getLoginUserType() {
        JwtUserLoginInfo jwtUserLoginInfo = getLoginUserInfo();
        if (jwtUserLoginInfo == null || jwtUserLoginInfo.getUserType() == null) {
            return null;
        }
        return jwtUserLoginInfo.getUserType();
    }

}
