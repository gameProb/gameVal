package com.project.gameVal.common.JWT.Service;


import com.project.gameVal.common.JWT.Entity.RefreshToken;
import com.project.gameVal.common.JWT.Exception.RefreshTokenExpiredException;
import com.project.gameVal.common.JWT.auth.JwtToken;

public interface RefreshTokenService {

    void saveOrUpdate(RefreshToken refreshToken);

    void deleteById(Long id);

    String findById(Long id);

    JwtToken reIssueTokens(RefreshToken refreshToken) throws RefreshTokenExpiredException;

    boolean isValidId(Long id);
}
