package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.ProbabilityPair;
import com.project.gameVal.web.probability.domain.ProbabilityTable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbabilityTableDTO {
    @NotBlank
    private String name;

    @NotEmpty(message = "Probabilities list must not be empty")
    private List<ProbabilityPair> probabilities;

    public ProbabilityTable toEntity(Long gameCompanyId) {
        return new ProbabilityTable(null, name, gameCompanyId, probabilities);
    }
}
