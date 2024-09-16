package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.entity.BlackListRefreshToken;
import com.project.gameVal.common.jwt.repository.BlackListRefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListRefreshTokenService {

    private final BlackListRefreshTokenRedisRepository blackListRefreshTokenRepository;

    public void save(BlackListRefreshToken blackListRefreshToken) {
        blackListRefreshTokenRepository.save(blackListRefreshToken);
    }

    public boolean exists(String refreshToken) {
        return blackListRefreshTokenRepository.existsById(refreshToken);
    }
}
