package com.project.gameVal.common.jwt.service;

import com.project.gameVal.common.jwt.dto.ValidateAccessTokenRequestDTO;

public interface AccessTokenService {

    boolean validateAccessToken(ValidateAccessTokenRequestDTO validateAccessTokenRequestDTO);
}
