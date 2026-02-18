package com.finalproject.infrastructure.common.fms;

public class SwingFileUrlResolver implements FileUrlResolver {
    private static final String PLACEHOLDER = "/resources/images/placeholder-cover.svg";

    @Override
    public String resolvePublicUrl(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return PLACEHOLDER;
        }
        return storageKey;
    }
}
