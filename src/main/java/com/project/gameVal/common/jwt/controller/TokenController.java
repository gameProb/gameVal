package com.project.gameVal.common.jwt.controller;

import com.project.gameVal.common.jwt.dto.request.ReIssueRefreshTokenRequestDTO;
import com.project.gameVal.common.jwt.dto.response.JWTsResponse;
import com.project.gameVal.common.jwt.exception.RefreshTokenExpiredException;
import com.project.gameVal.common.jwt.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<JWTsResponse> reIssueAccessToken(@RequestBody @Valid ReIssueRefreshTokenRequestDTO requestDTO)
            throws RefreshTokenExpiredException {
        try {
            log.info("start reissue token");

            String refreshToken = requestDTO.getRefreshToken();
            return new ResponseEntity<>(tokenService.reIssue(refreshToken), HttpStatus.OK);
        } catch (RefreshTokenExpiredException ex) {
            log.error("Refresh token expired: {}", ex.getMessage());
            throw ex; // 예외를 다시 던져서 글로벌 예외 핸들러에서 처리하도록 함
        }
    }
}