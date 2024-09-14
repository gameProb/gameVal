package com.project.gameVal.random.seed.dto.response;

import com.project.gameVal.random.seed.entity.RandomSeed;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentStateSeeds {

    private RandomSeed beforeSeed;

    private RandomSeed lastSeed;
}
