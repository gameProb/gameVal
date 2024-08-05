package com.project.gameVal.web.probability.controller.api;

import com.project.gameVal.common.jwt.auth.JWTUtil;
import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.dto.GameRegisterDTO;
import com.project.gameVal.web.probability.service.GameCompanyService;
import com.project.gameVal.web.probability.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/api")
public class GameAPIController {
    private final GameService gameService;
    private final GameCompanyService gameCompanyService;
    private final JWTUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<String> addGame(@RequestHeader("Authorization") String authorizationHeader, @RequestBody GameRegisterDTO gameRegisterDTO) {
        log.info("start add game");
        String accessToken = jwtUtil.getAccessTokenByAuthorizationHeader(authorizationHeader);
        Long gameCompanyId = jwtUtil.getIdByAccessToken(accessToken);

        GameCompany gameCompany = gameCompanyService.findById(gameCompanyId);
        gameRegisterDTO.setGameCompany(gameCompany);
        gameService.save(gameRegisterDTO);
        return new ResponseEntity<>("successfully game added", HttpStatus.CREATED);
    }
}
