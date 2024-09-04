package com.project.gameVal.common.jwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTsResponse {

    private String accessToken;

    private String refreshToken;
}
