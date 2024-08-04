package com.project.gameVal.web.probability.repository;

import com.project.gameVal.web.probability.domain.GameCompany;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCompanyRepository extends JpaRepository<GameCompany, Long> {
    Optional<GameCompany> findByName(String gameCompanyName);

    boolean existsByName(String gameCompanyName);
}
