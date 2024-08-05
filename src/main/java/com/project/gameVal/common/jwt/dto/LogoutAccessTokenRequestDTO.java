package com.project.gameVal.common.jwt.dto;

import com.project.gameVal.common.jwt.entity.LogoutAccessToken;
import com.project.gameVal.common.jwt.auth.JWTUtil;
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
