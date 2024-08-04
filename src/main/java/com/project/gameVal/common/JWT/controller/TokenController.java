package com.project.gameVal.common.JWT.controller;

import com.project.gameVal.common.JWT.DTO.RefreshTokenRequestDTO;
import com.project.gameVal.common.JWT.DTO.ValidateAccessTokenRequestDTO;
import com.project.gameVal.common.JWT.Exception.RefreshTokenExpiredException;
import com.project.gameVal.common.JWT.Service.AccessTokenService;
import com.project.gameVal.common.JWT.Service.RefreshTokenService;
import com.project.gameVal.common.JWT.auth.JWTUtil;
import com.project.gameVal.common.JWT.auth.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final AccessTokenService accessTokenService;

    private final RefreshTokenService refreshTokenService;

    private final JWTUtil jwtUtil;

    @Value("${jwt.accessToken.tokenPrefix}")
    private String tokenPrefix;

    @PostMapping("/validate")
    public ResponseEntity<?> validateAccessToken(@RequestHeader("Authorization") String authorizationHeader)
            throws RefreshTokenExpiredException {
        log.info("start validate access token");

        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenPrefix)) {
            return ResponseEntity.badRequest().body("Authorization header is missing or invalid.");
        }

        String accessToken = jwtUtil.getAccessTokenByAuthorizationHeader(authorizationHeader);
        ValidateAccessTokenRequestDTO validateAccessTokenRequestDTO = new ValidateAccessTokenRequestDTO(
                accessToken);
        if (accessTokenService.validateAccessToken(validateAccessTokenRequestDTO)) {
            return new ResponseEntity<>("validation success", HttpStatus.OK);
        }
        return new ResponseEntity<>("validation failed", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reIssueAccessToken(@CookieValue("refreshToken") String refreshToken)
            throws RefreshTokenExpiredException {
        log.info("start reissue token");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is missing or empty.");
        }

        // RefreshTokenRequestDTO 객체 생성 및 리프레시 토큰 설정
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO();
        refreshTokenRequestDTO.setRefreshToken(refreshToken);

        JwtToken jwtToken;
        try {
            jwtToken = refreshTokenService.reIssueTokens(refreshTokenRequestDTO.toEntity(jwtUtil));
        } catch (RefreshTokenExpiredException ex) {
            log.error("Refresh token expired: {}", ex.getMessage());
            throw ex; // 예외를 다시 던져서 글로벌 예외 핸들러에서 처리하도록 함
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken.getAccessToken());

        return ResponseEntity.ok().headers(headers).build();
    }
}