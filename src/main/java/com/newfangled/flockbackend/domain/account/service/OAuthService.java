package com.newfangled.flockbackend.domain.account.service;

import com.newfangled.flockbackend.domain.account.dto.AccountProfileDto;
import com.newfangled.flockbackend.domain.account.dto.OAuthLoginResponse;
import com.newfangled.flockbackend.domain.account.dto.OAuthTokenResponse;
import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.account.repository.AccountRepository;
import com.newfangled.flockbackend.domain.account.repository.InMemoryProviderRepository;
import com.newfangled.flockbackend.domain.account.type.OAuthProvider;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import com.newfangled.flockbackend.global.type.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token.expire-length:10000}")
    private long refreshTokenValidityInMilliseconds;

    public OAuthLoginResponse login(String providerName, String code) {
        // 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository 에서 OauthProvider 가져오기
        OAuthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        // access token 가져오기
        OAuthTokenResponse tokenResponse = getToken(code, provider);

        // 유저 정보 가져오기
        AccountProfileDto accountProfileDto = getUserProfile(providerName, tokenResponse, provider);

        // 유저 DB에 저장
        Account account = saveOrUpdate(accountProfileDto);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(account.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // Redis 에 데이터 삽입
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(String.valueOf(account.getId()), refreshToken);
        redisTemplate.expire(String.valueOf(account.getId()), refreshTokenValidityInMilliseconds,
                TimeUnit.MILLISECONDS);

        return OAuthLoginResponse.builder()
                .id(account.getId())
                .name(account.getOAuth().getName())
                .email(account.getOAuth().getEmail())
                .imageUrl(account.getOAuth().getProfileImage())
                .role(account.getRole())
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Account saveOrUpdate(AccountProfileDto accountProfileDto) {
        Account member = accountRepository.findByOAuth_OauthId(accountProfileDto.getOAuthId())
                .map(entity -> entity.update(
                        accountProfileDto.getEmail(), accountProfileDto.getName(), accountProfileDto.getImageUrl()))
                .orElseGet(accountProfileDto::toAccount);
        return accountRepository.save(member);
    }

    private OAuthTokenResponse getToken(String code, OAuthProvider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, OAuthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    private AccountProfileDto getUserProfile(String providerName, OAuthTokenResponse tokenResponse, OAuthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        return OAuthAttributes.extract(providerName, userAttributes);
    }

    // OAuth 서버에서 유저 정보 map 으로 가져오기
    private Map<String, Object> getUserAttributes(OAuthProvider provider, OAuthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
