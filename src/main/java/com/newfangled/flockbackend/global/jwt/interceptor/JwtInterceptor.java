package com.newfangled.flockbackend.global.jwt.interceptor;

import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null) {
            // 해더가 없음
            throw new JwtTokenProvider.JwtTokenException("token required");
        }

        if (jwtTokenProvider.validateToken(token)) {
            return true;
        }
        // 토큰이 잘못 됨
        throw new JwtTokenProvider.JwtTokenException("wrong token");
    }
}
