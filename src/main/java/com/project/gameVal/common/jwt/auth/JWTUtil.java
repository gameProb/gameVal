package com.project.gameVal.common.jwt.auth;

import com.project.gameVal.common.jwt.exception.AccessTokenExpiredException;
import com.project.gameVal.common.jwt.exception.AccessTokenNotExistException;
import com.project.gameVal.common.jwt.exception.RefreshTokenExpiredException;
import com.project.gameVal.common.jwt.exception.TokenNotValidException;
import com.project.gameVal.web.probability.domain.GameCompanyInformInToken;
import com.project.gameVal.web.probability.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${jwt.accessToken.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.accessToken.headerName}")
    private String accessTokenHeaderName;

    @Value("${jwt.accessToken.secret}")
    private String accessTokenSecretKey;

    @Value("${jwt.refreshToken.secret}")
    private String refreshTokenSecretKey;

    @Value("${jwt.accessToken.expirationMs}")
    private long accessTokenValidTime;

    @Value("${jwt.refreshToken.expirationMs}")
    private long refreshTokenValidTime;

    private Key getSigningAccessKey() {
        return new SecretKeySpec(accessTokenSecretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    private Key getSigningRefreshKey() {
        return new SecretKeySpec(refreshTokenSecretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createAccessToken(Long id, String name, Role role) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("gameCompanyId", id);
        claims.put("gameCompanyName", name);
        claims.put("role", role.toString());

        return Jwts.builder()
                //header

                //payload
                .setClaims(claims)
                .setIssuedAt(now) // token 생성 날짜
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                //signature
                .signWith(getSigningAccessKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long id, String name, Role role) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("gameCompanyId", id);
        claims.put("gameCompanyName", name);
        claims.put("role", role.toString());

        return Jwts.builder()
                //header

                //payload
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                //signature
                .signWith(getSigningRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateAccessToken(String accessToken) throws AccessTokenExpiredException, TokenNotValidException {
        try {
            // parsing 시에 검증 됨
            Jwts.parserBuilder()
                    .setSigningKey(getSigningAccessKey())
                    .build()
                    .parseClaimsJws(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (SignatureException e) {
            throw new TokenNotValidException();
        }
    }

    public void validateRefreshToken(String refreshToken) throws RefreshTokenExpiredException, TokenNotValidException {
        try {
            // parsing 시에 검증 됨
            Jwts.parserBuilder()
                    .setSigningKey(getSigningRefreshKey())
                    .build()
                    .parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (SignatureException e) {
            throw new TokenNotValidException();
        }
    }

    public Long getRemainingTimeByRefreshToken(String refreshToken) {
        long expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningRefreshKey())
                .build()
                .parseClaimsJws(refreshToken).getBody()
                .getExpiration().getTime();

        return expiration - new Date().getTime();
    }



    public String getAccessTokenByRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(accessTokenHeaderName);
        if (accessToken == null) {
            throw new AccessTokenNotExistException();
        }

        return accessToken.replace(tokenPrefix, "");
    }

    public GameCompanyInformInToken getGameCompanyInformInAccessToken(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningAccessKey())
                .build()
                .parseClaimsJws(accessToken).getBody();

        Long gameCompanyId = claims.get("gameCompanyId", Long.class);
        String gameCompanyName = claims.get("gameCompanyName", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        return new GameCompanyInformInToken(gameCompanyId, gameCompanyName, role);
    }

    public GameCompanyInformInToken getGameCompanyInformInRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningRefreshKey())
                .build()
                .parseClaimsJws(refreshToken).getBody();

        Long gameCompanyId = claims.get("gameCompanyId", Long.class);
        String gameCompanyName = claims.get("gameCompanyName", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        return new GameCompanyInformInToken(gameCompanyId, gameCompanyName, role);
    }
}
