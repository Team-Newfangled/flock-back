package com.newfangled.flockbackend.global.jwt.provider;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token.expire-length:10000}")
    private long accessTokenValidityInMilliseconds;
    @Value("${jwt.refresh-token.expire-length:10000}")
    private long refreshTokenValidityInMilliseconds;
    @Value("${jwt.token.secret-key:secret-key}")
    private String secretKey;

    public String createToken(long accountId, boolean isAccessToken) {
        return createToken(accountId,
                (isAccessToken ? accessTokenValidityInMilliseconds
                        : refreshTokenValidityInMilliseconds)
        );
    }

    public String createToken(long accountId, long expireLength) {
        Claims claims = Jwts.claims();
        claims.put("account", accountId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("authorization");
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getAccountId(String token) {
        Claims claims = parseClaims(token);

        if (claims.get("account") == null) {
            throw new JwtException("manipulated token");
        }
        return claims.get("account", Long.class);
    }

    public void insertRefreshToken(RedisTemplate<String, Object> redisTemplate,
                                   String payload, String refreshToken) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(payload, refreshToken);
        redisTemplate.expire(payload, refreshTokenValidityInMilliseconds,
                TimeUnit.MILLISECONDS);
    }

    @Getter
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class JwtTokenException extends RuntimeException {
        private final String message;

        public JwtTokenException(String message) {
            this.message = message;
        }
    }
}
