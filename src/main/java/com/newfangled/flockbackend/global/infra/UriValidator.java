package com.newfangled.flockbackend.global.infra;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UriValidator {

    private static final Pattern PATTERN
            = Pattern.compile("^(?:https?://)?(?:www\\.)?[a-zA-Z0-9./]+$");

    public boolean isUri(String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.matches()) {
            return true;
        }

        try {
            URL url = new URL(uri);
            url.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }

}
