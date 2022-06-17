package com.newfangled.flockbackend.global.jwt.provider;

import com.newfangled.flockbackend.domain.account.service.AuthDetailsService;
import com.newfangled.flockbackend.global.config.jwt.JwtConfiguration;
import com.newfangled.flockbackend.global.exception.BusinessException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String ACCESS = "ACCESS";
    private static final String REFRESH = "REFRESH";

    private final JwtConfiguration jwtConfiguration;
    private final AuthDetailsService authDetailsService;

    public String generateAccessToken(String loginId) {
        return generateToken(ACCESS, loginId, jwtConfiguration.getAccessToken());
    }

    public String generateRefreshToken(String loginId) {
        return generateToken(REFRESH, loginId, jwtConfiguration.getRefreshToken());
    }

    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfiguration.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractLoginIdFromToken(String token) {
        try {
            return parseToken(token).getSubject();
        } catch (SignatureException | MalformedJwtException e) {
            throw new TokenException("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenException("만료된 토큰입니다.");
        } catch (IllegalArgumentException | UnsupportedJwtException e) {
            throw new TokenException("비정상적인 토큰입니다.");
        }
    }

    private String generateToken(String type, String loginId, long expWithSecond) {
        final Date tokenCreationDate = new Date();

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtConfiguration.getSecretKey())
                .setSubject(loginId)
                .claim("type", type)
                .setIssuedAt(tokenCreationDate)
                .setExpiration(new Date(tokenCreationDate.getTime() + expWithSecond * 1000))
                .compact();
    }

    public String getTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }

    public Authentication getAuthenticationFromToken(String token) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(
                extractLoginIdFromToken(token)
        );
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }

    public static class TokenException extends BusinessException {
        public TokenException(String message) {
            super(HttpStatus.UNAUTHORIZED, message);
        }
    }
}
