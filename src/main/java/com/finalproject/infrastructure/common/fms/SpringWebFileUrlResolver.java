package com.finalproject.infrastructure.common.fms;

import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

public class SpringWebFileUrlResolver implements FileUrlResolver {
    private static final String PLACEHOLDER = "/resources/images/placeholder-cover.svg";

    @Override
    public String resolvePublicUrl(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return PLACEHOLDER;
        }

        return "/mvc/media/cover?path=" + UriUtils.encode(storageKey, StandardCharsets.UTF_8);
    }
}
