package com.project.gameVal.common.JWT.DTO;

import com.project.gameVal.common.JWT.Entity.LogoutAccessToken;
import com.project.gameVal.common.JWT.auth.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutAccessTokenRequestDTO {

    private String accessToken;

    public LogoutAccessToken toEntity(JWTUtil jwtUtil) {
        return LogoutAccessToken.builder()
                .accessTokenValue(accessToken)
                .expiration(jwtUtil.getRemainingTimeByAccessToken(accessToken))
                .build();
    }
}
