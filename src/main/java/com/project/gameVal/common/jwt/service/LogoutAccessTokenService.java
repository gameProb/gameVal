package com.project.gameVal.common.jwt.service;


import com.project.gameVal.common.jwt.entity.LogoutAccessToken;

public interface LogoutAccessTokenService {
    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    boolean existsByAccessToken(String accessToken);

    boolean isValid(String accessToken);
}
