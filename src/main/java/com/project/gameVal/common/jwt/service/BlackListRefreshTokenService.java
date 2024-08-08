package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.entity.BlackListRefreshToken;
import com.project.gameVal.common.jwt.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListRefreshTokenService {

    private final RefreshTokenRedisRepository refreshTokenRepository;

    public void save(BlackListRefreshToken blackListRefreshToken) {
        refreshTokenRepository.save(blackListRefreshToken);
    }

    public boolean exists(String refreshToken) {
        return refreshTokenRepository.existsById(refreshToken);
    }
}
