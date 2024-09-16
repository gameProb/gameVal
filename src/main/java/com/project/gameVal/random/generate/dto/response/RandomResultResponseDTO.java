package com.project.gameVal.random.generate.dto.response;

import com.project.gameVal.random.generate.entity.RandomResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RandomResultResponseDTO {

    private Double randomNumber;

    private String resultName;

    public RandomResult toEntity(Long gameId, UUID userUUID, String randomTableId, UUID sequenceId) {
        return RandomResult.builder()
                .gameId(gameId)
                .userUUID(userUUID)
                .randomTableId(randomTableId)
                .sequenceId(sequenceId)
                .randomNumber(randomNumber)
                .resultName(resultName)
                .build();
    }
}
