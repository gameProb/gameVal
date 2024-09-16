package com.project.gameVal.random.generate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class RandomSequences {

    private UUID id;

    private LocalDateTime sequenceStartTime;

    private Long sequenceDurationMs;

    private Integer sequenceLength;

    private List<List<Integer>> numberSequence;
}
