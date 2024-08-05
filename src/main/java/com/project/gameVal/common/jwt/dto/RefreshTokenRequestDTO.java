package com.project.gameVal.common.jwt.dto;

import com.project.gameVal.common.jwt.entity.RefreshToken;
import com.project.gameVal.common.jwt.auth.JWTUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    @NotBlank
    private String refreshToken;

    public RefreshToken toEntity(JWTUtil jwtUtil) {
        Long gameCompanyId = jwtUtil.getIdByRefreshToken(refreshToken);
        return RefreshToken.builder()
                .gameCompanyId(gameCompanyId)
                .refreshTokenValue(refreshToken)
                .expiration(jwtUtil.getRemainingTimeByRefreshToken(refreshToken)).build();
    }
}
