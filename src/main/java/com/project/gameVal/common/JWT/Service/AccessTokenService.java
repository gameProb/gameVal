package com.project.gameVal.common.JWT.Service;

import com.project.gameVal.common.JWT.DTO.ValidateAccessTokenRequestDTO;

public interface AccessTokenService {

    boolean validateAccessToken(ValidateAccessTokenRequestDTO validateAccessTokenRequestDTO);
}
