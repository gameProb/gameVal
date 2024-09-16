package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.Game;
import com.project.gameVal.web.probability.domain.GameCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRegisterDTO {
    @NotBlank
    @Size(min = 1, max = 50) // 문자열 길이 검증
    private String name;

    public Game toEntity(GameCompany gameCompany) {
        return new Game(null, name, gameCompany);
    }
}
