package com.newfangled.flockbackend.global.jwt.filter;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.global.exception.BusinessException;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String bearerToken = jwtTokenProvider.getTokenFromHeader(
                    request.getHeader("Authorization")
            );
            if (bearerToken != null) {
                Authentication authentication = jwtTokenProvider
                        .getAuthenticationFromToken(bearerToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (JwtTokenProvider.TokenException | Member.UnauthorizedException e) {
            filterExceptionHandle(e, response);
        }
    }

    private void filterExceptionHandle(BusinessException exception,
                                       HttpServletResponse response) throws IOException {
        response.setStatus(exception.getHttpStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(exception.getMessage());
    }
}
