package com.project.gameVal.web.probability.controller.api;

import com.project.gameVal.common.JWT.auth.JWTUtil;
import com.project.gameVal.web.probability.dto.GameCompanyRegisterDTO;
import com.project.gameVal.web.probability.service.GameCompanyService;
import jakarta.validation.Valid;
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
@RequestMapping("/company/api")
public class GameCompanyAPIController {
    private final GameCompanyService gameCompanyService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody GameCompanyRegisterDTO gameCompanyRegisterDTO) {
        gameCompanyService.register(gameCompanyRegisterDTO);
        return new ResponseEntity<>("successfully registered", HttpStatus.CREATED);
    }

    //login은 JwtAuthenticationFilter에서 진행

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        gameCompanyService.logout(jwtUtil.getAccessTokenByAuthorizationHeader(authorizationHeader));
        return new ResponseEntity<>("successfully logged out", HttpStatus.OK);
    }
}
