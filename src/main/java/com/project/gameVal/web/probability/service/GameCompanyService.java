package com.project.gameVal.web.probability.service;


import com.project.gameVal.web.probability.domain.GameCompany;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GameCompanyService extends UserDetailsService {
    void register(GameCompany gameCompany);

    Long findIdByName(String gameCompanyName);

    Optional<GameCompany> findById(Long id);

    void logout(String accessToken);
}
