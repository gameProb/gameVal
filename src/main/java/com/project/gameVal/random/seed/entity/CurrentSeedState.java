package com.project.gameVal.random.seed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentSeedState {

    private RandomSeed beforeSeed;

    private RandomSeed lastSeed;
}
