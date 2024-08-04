package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.domain.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCompanyRegisterDTO {
    @NotBlank @Min(4) @Max(20)
    private String name;

    @NotBlank @Min(4) @Max(30)
    private String password;

    public GameCompany toEntity() {
        return new GameCompany(null, name, password, Role.ROLE_GAME_COMPANY, new ArrayList<>());
    }
}
