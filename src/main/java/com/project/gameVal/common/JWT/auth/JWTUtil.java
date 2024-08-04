package com.project.gameVal.common.JWT.auth;

import com.project.gameVal.common.JWT.Exception.AccessTokenExpiredException;
import com.project.gameVal.common.JWT.Exception.AccessTokenNotExistException;
import com.project.gameVal.common.JWT.Exception.RefreshTokenExpiredException;
import com.project.gameVal.common.JWT.Exception.TokenNotValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    @Value("${jwt.accessToken.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.accessToken.SendingHeaderName}")
    private String accessTokenSendingHeaderName;

    @Value("${jwt.accessToken.GettingHeaderName}")
    private String accessTokenGettingHeaderName;

    @Value("${jwt.refreshToken.headerName}")
    private String refreshTokenHeaderName;

    @Value("${jwt.accessToken.secret}")
    private String accessTokenSecretKey;

    @Value("${jwt.refreshToken.secret}")
    private String refreshTokenSecretKey;

    @Value("${jwt.accessToken.expirationMs}")
    private long accessTokenValidTime;

    @Value("${jwt.refreshToken.expirationMs}")
    private long refreshTokenValidTime;

    @PostConstruct
    protected void init() {
        accessTokenSecretKey = Base64.getEncoder().encodeToString(accessTokenSecretKey.getBytes());
        refreshTokenSecretKey = Base64.getEncoder().encodeToString(refreshTokenSecretKey.getBytes());
    }

    private Key getSigningAccessKey() {
        byte[] keyBytes = Base64.getDecoder().decode(accessTokenSecretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Key getSigningRefreshKey() {
        byte[] keyBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String createAccessToken(Long id, String name) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("gameCompanyId", id.toString());
        claims.put("gameCompanyName", name);

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

    public String createRefreshToken(Long id, String name) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("gameCompanyId", id.toString());
        claims.put("gameCompanyName", name);

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

    public String getNameByAccessToken(String accessToken) throws AccessTokenExpiredException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningAccessKey())
                .build()
                .parseClaimsJws(accessToken).getBody()
                .get("gameCompanyName", String.class);
    }


    public String getNameByRefreshToken(String refreshToken) throws RefreshTokenExpiredException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningRefreshKey())
                .build()
                .parseClaimsJws(refreshToken).getBody()
                .get("gameCompanyName", String.class);
    }

    public Long getIdByAccessToken(String accessToken) throws AccessTokenExpiredException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningAccessKey())
                .build()
                .parseClaimsJws(accessToken).getBody();

        String idString = claims.get("gameCompanyId", String.class);
        if (idString == null) {
            return Long.parseLong(claims.getSubject());
        }

        return Long.parseLong(idString);
    }

    public Long getIdByRefreshToken(String refreshToken) throws RefreshTokenExpiredException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningRefreshKey())
                .build()
                .parseClaimsJws(refreshToken).getBody();

        String idString = claims.get("gameCompanyId", String.class);
        if (idString == null) {
            return Long.parseLong(claims.getSubject());
        }

        return Long.parseLong(idString);
    }

    public Long getRemainingTimeByAccessToken(String accessToken) {
        long expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningAccessKey())
                .build()
                .parseClaimsJws(accessToken).getBody()
                .getExpiration().getTime();

        return (expiration - new Date().getTime()) / 1000;
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
        String accessToken = request.getHeader(accessTokenGettingHeaderName);
        if (accessToken == null) {
            throw new AccessTokenNotExistException();
        }

        return accessToken.replace(tokenPrefix, "");
    }

    public String getAccessTokenByAuthorizationHeader(String header) {
        if (header == null) {
            throw new AccessTokenNotExistException();
        }
        return header.replace(tokenPrefix, "");
    }
}
