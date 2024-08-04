package com.project.gameVal.common.JWT.Service;


import com.project.gameVal.common.JWT.Entity.LogoutAccessToken;

public interface LogoutAccessTokenService {
    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    boolean existsByAccessToken(String accessToken);

    boolean isValid(String accessToken);
}
