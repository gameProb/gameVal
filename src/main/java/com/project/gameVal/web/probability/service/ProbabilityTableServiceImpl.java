package com.project.gameVal.web.probability.service;

import com.project.gameVal.web.probability.domain.ProbabilityPair;
import com.project.gameVal.web.probability.domain.ProbabilityTable;
import com.project.gameVal.web.probability.dto.ProbabilityTableDTO;
import com.project.gameVal.web.probability.exception.ProbabilityTableException;
import com.project.gameVal.web.probability.repository.ProbabilityTableRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProbabilityTableServiceImpl implements ProbabilityTableService {
    private final ProbabilityTableRepository probabilityTableRepository;
    private static final double MIN_PROBABILITY = 0.0;
    private static final double MAX_PROBABILITY = 1.0;

    @Override
    public ProbabilityTable findById(String tableId) {
        return probabilityTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
    }

    @Override
    public ProbabilityTable save(ProbabilityTable probabilityTable) {
        return probabilityTableRepository.save(probabilityTable);
    }

    @Override
    public ProbabilityTable createProbabilityTable(Long gameCompanyId, ProbabilityTableDTO probabilityTableDTO) {
        List<ProbabilityPair> probabilities = calculateCumulativeProbabilities(probabilityTableDTO.getProbabilities());
        probabilityTableDTO.setProbabilities(probabilities);
        return save(probabilityTableDTO.toEntity(gameCompanyId));
    }

    private List<ProbabilityPair> calculateCumulativeProbabilities(List<ProbabilityPair> originalProbabilities) {
        List<ProbabilityPair> cumulativeProbabilities = new ArrayList<>();
        double sum = 0.0;
        for (ProbabilityPair pair : originalProbabilities) {
            validateProbabilityRange(pair.getProbability());
            sum += pair.getProbability();
            cumulativeProbabilities.add(new ProbabilityPair(sum, pair.getResult()));
        }
        validateProbabilitySum(sum);
        return cumulativeProbabilities;
    }

    private void validateProbabilitySum(Double probabilitySum) {
        if (probabilitySum != MAX_PROBABILITY) {
            throw new IllegalArgumentException(
                    "Invalid probability values. The sum of probabilities must be " + MAX_PROBABILITY + ", but was "
                            + probabilitySum);
        }
    }

    @Override
    public String findResultByTargetValue(String tableId, Double target) {
        validateProbabilityRange(target);
        ProbabilityTable table = findTableById(tableId);
        return findResultProcess(table.getProbabilities(), target);
    }

    private void validateProbabilityRange(Double probability) {
        if (probability <= MIN_PROBABILITY || probability > MAX_PROBABILITY) {
            throw new IllegalArgumentException("Invalid probability value. Must be between "
                    + MIN_PROBABILITY + " and " + MAX_PROBABILITY);
        }
    }

    private ProbabilityTable findTableById(String tableId) {
        return Optional.ofNullable(findById(tableId))
                .orElseThrow(() -> new ProbabilityTableException("Table not found with id: " + tableId));
    }

    private String findResultProcess(List<ProbabilityPair> probabilities, Double target) {
        return probabilities.stream()
                .filter(pair -> target <= pair.getProbability())
                .findFirst()
                .map(ProbabilityPair::getResult)
                .orElseThrow(() -> new ProbabilityTableException("No result found for target: " + target));
    }
}
