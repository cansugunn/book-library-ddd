package com.finalproject.domain.valueobject;

import java.util.Objects;

public class Website {
    private final String url;

    public Website(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return Objects.equals(url, website.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }
}
