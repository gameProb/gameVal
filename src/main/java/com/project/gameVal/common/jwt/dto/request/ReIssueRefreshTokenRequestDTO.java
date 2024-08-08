package com.project.gameVal.common.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReIssueRefreshTokenRequestDTO {

    @NotBlank
    private String refreshToken;
}
