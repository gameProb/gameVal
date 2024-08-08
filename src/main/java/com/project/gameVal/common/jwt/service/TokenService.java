package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.auth.JWTUtil;
import com.project.gameVal.common.jwt.dto.response.JWTsResponse;
import com.project.gameVal.common.jwt.entity.BlackListRefreshToken;
import com.project.gameVal.common.jwt.exception.TokenNotValidException;
import com.project.gameVal.web.probability.domain.GameCompanyInformInToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTUtil jwtUtil;

    private final BlackListRefreshTokenService blackListRefreshTokenService;

    public void blackListToken(String refreshToken) {
        blackListRefreshTokenService.save(new BlackListRefreshToken(refreshToken, jwtUtil.getRemainingTimeByRefreshToken(refreshToken)));
    }

    public Boolean isBlackListed(String refreshToken) {
        return blackListRefreshTokenService.exists(refreshToken);
    }

    public JWTsResponse reIssue(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        if (isBlackListed(refreshToken)) throw new TokenNotValidException();

        GameCompanyInformInToken gameCompanyInform = jwtUtil.getGameCompanyInformInRefreshToken(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken(gameCompanyInform.getId(), gameCompanyInform.getName(), gameCompanyInform.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(gameCompanyInform.getId(), gameCompanyInform.getName(), gameCompanyInform.getRole());

        blackListToken(refreshToken);

        return new JWTsResponse(newAccessToken, newRefreshToken);
    }
}
