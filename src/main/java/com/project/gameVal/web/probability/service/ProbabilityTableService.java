package com.project.gameVal.web.probability.service;

import com.project.gameVal.web.probability.domain.ProbabilityTable;
import com.project.gameVal.web.probability.dto.ProbabilityTableDTO;

public interface ProbabilityTableService {
    ProbabilityTable findById(String id);

    ProbabilityTable save(ProbabilityTable probabilityTable);

    ProbabilityTable createProbabilityTable(ProbabilityTableDTO probabilityTableDTO);

    String findResultByTargetValue(String tableId, Double target);
}
