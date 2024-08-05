package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.entity.RefreshToken;
import com.project.gameVal.common.jwt.exception.RefreshTokenNotExistException;
import com.project.gameVal.common.jwt.exception.TokenNotValidException;
import com.project.gameVal.common.jwt.repository.RefreshTokenRedisRepository;
import com.project.gameVal.common.jwt.auth.JWTUtil;
import com.project.gameVal.common.jwt.auth.JwtToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JWTUtil jwtUtil;

    @Override
    @Transactional
    public void saveOrUpdate(RefreshToken refreshToken) {
        refreshTokenRedisRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        refreshTokenRedisRepository.deleteById(id);
    }

    @Override
    public String findById(Long id) throws RefreshTokenNotExistException {
        return refreshTokenRedisRepository.findById(id)
                .orElseThrow(RefreshTokenNotExistException::new).getRefreshTokenValue();
    }

    @Override
    @Transactional
    public JwtToken reIssueTokens(RefreshToken refreshToken)
            throws RefreshTokenNotExistException, TokenNotValidException {
        String refreshTokenValue = refreshToken.getRefreshTokenValue();
        Long id = jwtUtil.getIdByRefreshToken(refreshTokenValue);

        jwtUtil.validateRefreshToken(refreshTokenValue);
        if (!isValidId(id)) {
            throw new TokenNotValidException();
        }

        String name = jwtUtil.getNameByRefreshToken(refreshTokenValue);
        String newRefreshToken = jwtUtil.createRefreshToken(id, name);

        saveOrUpdate(
                RefreshToken.builder()
                        .gameCompanyId(id)
                        .refreshTokenValue(newRefreshToken)
                        .expiration(jwtUtil.getRemainingTimeByRefreshToken(newRefreshToken))
                        .build()
        );

        return new JwtToken(jwtUtil.createAccessToken(id, name), newRefreshToken);
    }

    @Override
    public boolean isValidId(Long id) {
        return refreshTokenRedisRepository.existsById(id);
    }
}
