package com.project.gameVal.web.probability.controller.api;

import com.project.gameVal.web.probability.domain.GameCompanyInformInToken;
import com.project.gameVal.web.probability.dto.GameRegisterDTO;
import com.project.gameVal.web.probability.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/game/api")
public class GameAPIController {
    private final GameService gameService;

    @PostMapping("/add")
    public ResponseEntity<String> addGame(@AuthenticationPrincipal GameCompanyInformInToken gameCompanyInform, @RequestBody GameRegisterDTO gameRegisterDTO) {
        log.info("start add game");

        gameService.save(gameCompanyInform.getId(), gameRegisterDTO);
        return new ResponseEntity<>("successfully game added", HttpStatus.CREATED);
    }
}
