package com.project.gameVal.web.probability.service;

import com.project.gameVal.common.JWT.DTO.LogoutAccessTokenRequestDTO;
import com.project.gameVal.common.JWT.Service.LogoutAccessTokenService;
import com.project.gameVal.common.JWT.Service.RefreshTokenService;
import com.project.gameVal.common.JWT.auth.JWTUtil;
import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.domain.PrincipalDetails;
import com.project.gameVal.web.probability.dto.GameCompanyRegisterDTO;
import com.project.gameVal.web.probability.exception.GameCompanyAlreadyExistException;
import com.project.gameVal.web.probability.exception.GameCompanyNotFoundException;
import com.project.gameVal.web.probability.repository.GameCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameCompanyServiceImpl implements GameCompanyService {
    private final GameCompanyRepository gameCompanyRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final LogoutAccessTokenService logoutAccessTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String gameCompanyName) throws UsernameNotFoundException {
        GameCompany gameCompany = gameCompanyRepository.findByName(gameCompanyName)
                .orElseThrow(GameCompanyNotFoundException::new);
        return new PrincipalDetails(gameCompany);
    }

    @Override
    public Long findIdByName(String gameCompanyName) throws GameCompanyNotFoundException {
        return gameCompanyRepository.findByName(gameCompanyName).orElseThrow(GameCompanyNotFoundException::new).getId();
    }

    @Override
    @Transactional
    public void register(GameCompanyRegisterDTO gameCompanyRegisterDTO) {
        if (gameCompanyRepository.existsByName(gameCompanyRegisterDTO.getName())) {
            log.warn("GameCompanyAlreadyExistException in register");
            throw new GameCompanyAlreadyExistException();
        }
        GameCompany gameCompany = gameCompanyRegisterDTO.toEntity();
        gameCompany.setPassword(passwordEncoder.encode(gameCompany.getPassword()));
        gameCompanyRepository.save(gameCompany);
    }

    @Override
    public GameCompany findById(Long id) {
        return gameCompanyRepository.findById(id)
                .orElseThrow(GameCompanyNotFoundException::new);
    }

    @Override
    @Transactional
    public void logout(String accessToken) {
        logoutAccessTokenService.saveLogoutAccessToken(new LogoutAccessTokenRequestDTO(accessToken).toEntity(jwtUtil));
        refreshTokenService.deleteById(jwtUtil.getIdByAccessToken(accessToken));
    }
}
