package com.project.gameVal.web.probability.dto;

import com.project.gameVal.web.probability.domain.ProbabilityPair;
import com.project.gameVal.web.probability.domain.ProbabilityTable;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private List<ProbabilityPair> probabilities;

    public ProbabilityTable toEntity(Long gameCompanyId) {
        return new ProbabilityTable(null, name, gameCompanyId, probabilities);
    }
}
