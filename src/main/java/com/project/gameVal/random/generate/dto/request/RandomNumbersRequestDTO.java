package com.project.gameVal.random.generate.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RandomNumbersRequestDTO {

    @NotNull
    private Long gameId;

    @NotNull
    private UUID userUUID;

    @NotNull
    private String randomTableId;

    @NotNull
    private LocalDateTime executionTime;

    @NotNull
    @Min(1)
    @Max(value = 1000) // generate.random.maxCount, application.yml에 있는 값을 가져와서 사용할 수 없음
    private Integer count;
}
