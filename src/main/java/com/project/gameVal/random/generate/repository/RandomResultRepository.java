package com.project.gameVal.random.generate.repository;

import com.project.gameVal.random.generate.entity.RandomResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RandomResultRepository extends JpaRepository<RandomResult, Long> {

    List<RandomResult> findAllByUserUUIDAndGameIdOrderBySequenceIdAsc(UUID userUUID, Long gameId, Pageable pageable);
}
