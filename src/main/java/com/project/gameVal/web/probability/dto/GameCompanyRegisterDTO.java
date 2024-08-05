package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCompanyRegisterDTO {
    @NotBlank
    @Size(min = 4, max = 20) // 문자열 길이 검증
    private String name;

    @NotBlank
    @Size(min = 4, max = 70) // 문자열 길이 검증
    private String password;

    public GameCompany toEntity() {
        return new GameCompany(null, name, password, Role.ROLE_GAME_COMPANY, new ArrayList<>());
    }
}
