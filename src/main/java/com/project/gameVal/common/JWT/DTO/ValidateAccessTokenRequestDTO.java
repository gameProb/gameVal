package com.project.gameVal.common.JWT.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  //무조건 필요함
public class ValidateAccessTokenRequestDTO {
    @NotBlank
    private String accessToken;
}
