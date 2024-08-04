package com.project.gameVal.common.JWT.auth;

import com.project.gameVal.common.JWT.Exception.AccessTokenExpiredException;
import com.project.gameVal.common.JWT.Exception.AccessTokenNotExistException;
import com.project.gameVal.common.JWT.Exception.TokenNotValidException;
import com.project.gameVal.common.JWT.Service.LogoutAccessTokenService;
import com.project.gameVal.web.probability.domain.GameCompany;
import com.project.gameVal.web.probability.domain.PrincipalDetails;
import com.project.gameVal.web.probability.exception.GameCompanyNotFoundException;
import com.project.gameVal.web.probability.service.GameCompanyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final GameCompanyService gameCompanyService;

    private final RequestMatcher excludeRequestMatcher;

    private final LogoutAccessTokenService logoutAccessTokenService;

    @Builder
    public JWTAuthorizationFilter(JWTUtil jwtUtil, GameCompanyService gameCompanyService,
                                  LogoutAccessTokenService logoutAccessTokenService) {
        this.jwtUtil = jwtUtil;
        this.gameCompanyService = gameCompanyService;
        this.logoutAccessTokenService = logoutAccessTokenService;
        this.excludeRequestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher("/token/reIssue", HttpMethod.POST.name()),

                new AntPathRequestMatcher("/", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/login", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/company/api/register", HttpMethod.POST.name()),

                new AntPathRequestMatcher("/test", HttpMethod.GET.name()), //TODO form login 제외 시 해제

                new AntPathRequestMatcher("/health-check", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/token/validate", HttpMethod.POST.name()),

                new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name()),

                new AntPathRequestMatcher("/assets/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/js/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/css/**", HttpMethod.GET.name())
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("start jwt authorization : {}", request.getRequestURL());

            String accessToken = jwtUtil.getAccessTokenByRequest(request);

            jwtUtil.validateAccessToken(accessToken);
            if (!logoutAccessTokenService.isValid(accessToken)) {
                throw new TokenNotValidException();
            }

            Long gameCompanyId = jwtUtil.getIdByAccessToken(accessToken);
            GameCompany gameCompany = gameCompanyService.findById(gameCompanyId);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            gameCompany,
                            null,
                            new PrincipalDetails(gameCompany).getAuthorities()
                    )
            );

            log.info("jwt authorization success : {}", request.getRequestURL());
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException e) {
            log.debug("AccessToken Expired: {}", e.getMessage());
            response.sendError(e.getHttpStatus().value(), e.getMessage());
        } catch (AccessTokenNotExistException e) {
            log.debug("Token Not Exist: {}", e.getMessage());
            response.sendError(e.getHttpStatus().value(), e.getMessage());
        } catch (TokenNotValidException e) { //
            log.debug("Token Not Valid: {}", e.getMessage());
            response.sendError(e.getHttpStatus().value(), e.getMessage());
        } catch (GameCompanyNotFoundException e) {
            log.debug("GameCompany Not Found: {}", e.getMessage());
            response.sendError(e.getHttpStatus().value(), e.getMessage());
        } catch (Exception e) {
            log.error("unhandled error: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "처리되지 않은 에러입니다.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeRequestMatcher.matches(request);
    }
}
