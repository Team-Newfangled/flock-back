package com.newfangled.flockbackend.controller;

import com.newfangled.flockbackend.domain.member.entity.Member;
import com.newfangled.flockbackend.domain.member.repository.MemberRepository;
import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ControllerTestUtil {

    private static MockHttpServletRequestBuilder method(String method, String uri) {
        if (method.equalsIgnoreCase("POST")) {
            return post(uri);
        }
        else if (method.equalsIgnoreCase("GET")) {
            return get(uri);
        }
        else if (method.equalsIgnoreCase("PATCH")) {
            return patch(uri);
        }
        else if (method.equalsIgnoreCase("DELETE")) {
            return delete(uri);
        }
        return head(uri);
    }

    public static ResultActions resultActions(MockMvc mockMvc, String uri,
                                              String content, String method,
                                              String token) throws Exception {
        return mockMvc.perform(
                method(method, uri)
                        .content((content == null) ? "" : content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", (token == null) ? "" : token)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    public static String getAccessToken(JwtTokenProvider jwtTokenProvider) {
        return String.format("Bearer %s", jwtTokenProvider.generateAccessToken("1"));
    }

    public static String getFakeToken() {
        return "Bearer NULL";
    }

    public static void authenticateStumpMember(Member member,
                                               MemberRepository memberRepository) {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
    }

    public static String randomString() {
        return new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                )
                .toString();
    }
}
