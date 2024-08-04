package com.project.gameVal.common.JWT.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
