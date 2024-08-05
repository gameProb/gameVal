package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.dto.ValidateAccessTokenRequestDTO;
import com.project.gameVal.common.jwt.auth.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JWTUtil jwtUtil;

    @Override
    public boolean validateAccessToken(ValidateAccessTokenRequestDTO validateAccessTokenRequestDTO) {
        try {
            jwtUtil.validateAccessToken(validateAccessTokenRequestDTO.getAccessToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
