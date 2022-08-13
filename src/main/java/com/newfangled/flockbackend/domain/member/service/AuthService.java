package com.newfangled.flockbackend.domain.member.service;

import com.newfangled.flockbackend.domain.member.dto.request.TokenRefreshRequest;
import com.newfangled.flockbackend.domain.member.dto.response.OAuthResponseDto;
import com.newfangled.flockbackend.domain.member.dto.response.TokenRefreshResponse;
import com.newfangled.flockbackend.domain.member.embed.OAuth;
import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.domain.member.type.UserRole;
import com.newfangled.flockbackend.global.config.infra.oauth.GoogleAuthConfiguration;
import com.newfangled.flockbackend.global.infra.oauth.google.GoogleApiService;
import com.newfangled.flockbackend.global.infra.oauth.google.GoogleAuthService;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final GoogleApiService googleApiService;
    private final GoogleAuthConfiguration googleAuthConfiguration;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public TokenRefreshResponse refreshToken(
            TokenRefreshRequest tokenRefreshRequest) {
        return new TokenRefreshResponse(
                jwtTokenProvider.generateAccessToken(
                        jwtTokenProvider.extractLoginIdFromToken(tokenRefreshRequest.getRefreshToken())
                )
        );
    }

    @Transactional
    public OAuthResponseDto oAuthGoogle(String code) {
        GoogleAuthService.GetAccessTokenResponse accessTokenResponse
                = getAccessTokenResponse(code);
        log.info(accessTokenResponse.toString());
        GoogleApiService.GetUserInfoResponse userInfoResponse
                = getUserInfoResponse(accessTokenResponse.getAccessToken());
        log.info(userInfoResponse.toString());

        Member account = memberRepository.findByOAuth_OauthId(userInfoResponse.getOpenId())
                .orElseGet(() -> joinOAuthUser(userInfoResponse));

        return new OAuthResponseDto(
                jwtTokenProvider.generateAccessToken(account.getUsername()),
                jwtTokenProvider.generateRefreshToken(account.getUsername())
        );
    }

    private GoogleAuthService
            .GetAccessTokenResponse getAccessTokenResponse(String code) {
        try {
            Response<GoogleAuthService.GetAccessTokenResponse> tokenResponse
                    = googleAuthService.getAccessToken(
                            code,
                            googleAuthConfiguration.getClientId(),
                            googleAuthConfiguration.getClientSecret(),
                            "authorization_code",
                            googleAuthConfiguration.getRedirectUri()
            ).execute();
            if (!tokenResponse.isSuccessful()) {
                throw new Member.FailedToAuthException();
            }
            return tokenResponse.body();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new Member.FailedToAuthException();
        }
    }

    private GoogleApiService
            .GetUserInfoResponse getUserInfoResponse(String accessToken) {
        try {
            Response<GoogleApiService.GetUserInfoResponse> userInfoResponse
                    = googleApiService.getInfo(
                    String.format("Bearer %s", accessToken)
            ).execute();
            if (!userInfoResponse.isSuccessful()) {
                assert userInfoResponse.errorBody() != null;
                log.error("{}", new String(userInfoResponse.errorBody().bytes()));
                throw new Member.FailedToAuthException();
            }
            return userInfoResponse.body();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new Member.FailedToAuthException();
        }
    }

    @Transactional
    public Member joinOAuthUser(GoogleApiService
                                             .GetUserInfoResponse userInfoResponse) {
        OAuth oAuth = OAuth.builder()
                .name(userInfoResponse.getGivenName())
                .oauthId(userInfoResponse.getOpenId())
                .pictureUrl(userInfoResponse.getProfileImageUrl())
                .email(userInfoResponse.getEmail())
                .build();
        Member account = Member.builder()
                .role(UserRole.MEMBER)
                .oAuth(oAuth)
                .build();
        return memberRepository.save(account);
    }
}
