package com.newfangled.flockbackend.controller;

import com.newfangled.flockbackend.global.jwt.provider.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
                        .content(content)
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
}
