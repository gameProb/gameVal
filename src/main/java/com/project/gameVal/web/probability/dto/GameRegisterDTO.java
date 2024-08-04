package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.Game;
import com.project.gameVal.web.probability.domain.GameCompany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRegisterDTO {
    @NotBlank @Max(50)
    private String name;

    private GameCompany gameCompany;

    public Game toEntity() {
        return new Game(null, name, gameCompany);
    }
}
