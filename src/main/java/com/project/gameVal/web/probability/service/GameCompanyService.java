package com.project.gameVal.web.probability.service;


import com.project.gameVal.web.probability.domain.GameCompany;
import java.util.Optional;

public interface GameCompanyService {
    void register(GameCompany gameCompany);

    Long findIdByName(String gameCompanyName);

    Optional<GameCompany> findById(Long id);

    void logout(String accessToken);
}
