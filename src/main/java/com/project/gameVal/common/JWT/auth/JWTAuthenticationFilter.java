package com.project.gameVal.common.JWT.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gameVal.common.JWT.DTO.RefreshTokenRequestDTO;
import com.project.gameVal.common.JWT.Service.RefreshTokenService;
import com.project.gameVal.web.probability.domain.PrincipalDetails;
import com.project.gameVal.web.probability.dto.GameCompanyLoginDTO;
import com.project.gameVal.web.probability.service.GameCompanyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final GameCompanyService gameCompanyService;

    @Builder
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                   JWTUtil jwtUtil, RefreshTokenService refreshTokenService,
                                   GameCompanyService gameCompanyService) throws Exception {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.gameCompanyService = gameCompanyService;

        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("attempt Authentication");
        GameCompanyLoginDTO gameCompanyLoginDTO = null;

        try {
            // 이 부분을 위해서 MemberRequestDTO에 NoArgsConStructor가 필요함
            gameCompanyLoginDTO = objectMapper.readValue(request.getInputStream(), GameCompanyLoginDTO.class);
        } catch (Exception e) {
            log.error("jwtAuthenticationFilter error");
        }
        assert gameCompanyLoginDTO != null;

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                gameCompanyLoginDTO.getName(), gameCompanyLoginDTO.getPassword());

        // 이곳에서 authenticate하기 위해 CustomUserDetailsService의 loadUserByUsername()이 실행됨
        // 따라서 loadUserByUsername()에서는 UserDetails를 구현한 클래스를 return해야함
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authResult)
            throws IOException, ServletException {
        log.info("authentication success");

        String gameCompanyName = ((PrincipalDetails) authResult.getPrincipal()).getUsername();

        Long gameCompanyId = gameCompanyService.findIdByName(gameCompanyName);

        String accessToken = jwtUtil.createAccessToken(gameCompanyId, gameCompanyName);
        String refreshToken = jwtUtil.createRefreshToken(gameCompanyId, gameCompanyName);

        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO(refreshToken);

        refreshTokenService.saveOrUpdate(refreshTokenRequestDTO.toEntity(jwtUtil)); // 새로 발급한 refreshToken 저장

        // header에 token 작성
        response.setHeader("Authorization", accessToken);

        // Refresh Token을 쿠키에 저장 (클라이언트에서 이를 접근할 수 있는지 확인)
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);  // JavaScript에서 접근할 수 없도록 설정 -> XSS 공격 방지
        refreshCookie.setPath("/");  // 모든 경로에서 접근 가능하도록 설정
        response.addCookie(refreshCookie);

        // SameSite 속성 설정 (브라우저에 따라 다를 수 있음)
        refreshCookie.setAttribute("SameSite", "Strict"); //CSRF 공격 방지

        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("authentication fail");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
