package com.project.gameVal.web.probability.repository;

import com.project.gameVal.web.probability.domain.Game;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(@NonNull String gameName);
}
