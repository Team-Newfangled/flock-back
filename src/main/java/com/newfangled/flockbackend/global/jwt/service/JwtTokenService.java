package com.newfangled.flockbackend.global.jwt.service;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.global.config.redis.Redis;
import com.newfangled.flockbackend.global.jwt.dto.response.TokenResponseDto;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenResponseDto tokenReissue(String refreshToken) {
        // 만료된 토큰
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtException("refresh_token is not valid! please login again");
        }

        // payload 를 가져와서 회원을 찾는다.
        Long payloadAccountId = jwtTokenProvider.getAccountId(refreshToken);

        Account account = accountRepository.findById(payloadAccountId)
                .orElseThrow(() -> new Account.NotExisted(payloadAccountId));

        // 해당 회원의 리프레시 토큰이 redis 에 저장되어 있는 지 파악
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        if (operations.getAndDelete(String.valueOf(account.getId())) == null) {
            throw new Redis.InvalidKey(String.valueOf(account.getId()));
        }

        // 다시 accessToken 및 refresh token 저장 및 반환
        String accessToken = jwtTokenProvider.createToken(account.getId(), true);
        String newRefreshToken = jwtTokenProvider.createToken(account.getId(), false);

        // Redis 에 데이터 삽입
        jwtTokenProvider.insertRefreshToken(
                redisTemplate,
                String.valueOf(account.getId()),
                newRefreshToken
        );
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("authorization")
                .build();
    }
}
