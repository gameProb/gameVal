package com.project.gameVal.common.jwt.service;


import com.project.gameVal.common.jwt.entity.RefreshToken;
import com.project.gameVal.common.jwt.exception.RefreshTokenExpiredException;
import com.project.gameVal.common.jwt.auth.JwtToken;

public interface RefreshTokenService {

    void saveOrUpdate(RefreshToken refreshToken);

    void deleteById(Long id);

    String findById(Long id);

    JwtToken reIssueTokens(RefreshToken refreshToken) throws RefreshTokenExpiredException;

    boolean isValidId(Long id);
}
