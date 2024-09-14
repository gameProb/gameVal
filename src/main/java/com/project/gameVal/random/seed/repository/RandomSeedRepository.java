package com.project.gameVal.random.seed.repository;

import com.project.gameVal.random.seed.entity.RandomSeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RandomSeedRepository extends JpaRepository<RandomSeed, Long> {

    List<RandomSeed> findTop2ByOrderBySeedStartTimeDesc();

    RandomSeed findBySeedStartTime(LocalDateTime seedStartTime);
}
