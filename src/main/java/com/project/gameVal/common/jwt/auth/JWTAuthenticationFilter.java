package com.project.gameVal.common.jwt.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gameVal.common.jwt.dto.response.JWTsResponse;
import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.domain.PrincipalDetails;
import com.project.gameVal.web.probability.domain.Role;
import com.project.gameVal.web.probability.dto.GameCompanyLoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;

    @Builder
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                   JWTUtil jwtUtil) throws Exception {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;

        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("attempt Authentication");

        try {
            // 이 부분을 위해서 MemberRequestDTO에 NoArgsConStructor가 필요함
            GameCompanyLoginDTO gameCompanyLoginDTO = objectMapper.readValue(request.getInputStream(), GameCompanyLoginDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    gameCompanyLoginDTO.getName(), gameCompanyLoginDTO.getPassword());

            // 이곳에서 authenticate하기 위해 CustomUserDetailsService의 loadUserByUsername()이 실행됨
            // 따라서 loadUserByUsername()에서는 UserDetails를 구현한 클래스를 return해야함
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            log.error("jwtAuthenticationFilter error");

            throw new BadCredentialsException("login fail");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authResult)
            throws IOException, ServletException {
        log.info("authentication success");

        GameCompany gameCompany = ((PrincipalDetails) authResult.getPrincipal()).getGameCompany();
        Long gameCompanyId = gameCompany.getId();
        String gameCompanyName = gameCompany.getName();
        Role role = gameCompany.getRole();


        String accessToken = jwtUtil.createAccessToken(gameCompanyId, gameCompanyName, role);
        String refreshToken = jwtUtil.createRefreshToken(gameCompanyId, gameCompanyName, role);

        String tokens = objectMapper.writeValueAsString(new JWTsResponse(accessToken, refreshToken));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("authentication fail");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
