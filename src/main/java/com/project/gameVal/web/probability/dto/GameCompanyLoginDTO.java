package com.project.gameVal.web.probability.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCompanyLoginDTO {
    @NotBlank
    @Size(min = 4, max = 20) // 문자열 길이 검증
    private String name;

    @NotBlank
    @Size(min = 4, max = 70) // 문자열 길이 검증
    private String password;
}
