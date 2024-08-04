package com.project.gameVal.web.probability.service;


import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.dto.GameCompanyRegisterDTO;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface GameCompanyService extends UserDetailsService {
    void register(GameCompanyRegisterDTO gameCompanyRegisterDTO);

    Long findIdByName(String gameCompanyName);

    Optional<GameCompany> findById(Long id);

    void logout(String accessToken);
}
