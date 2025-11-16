package com.finalproject.domain.valueobject;

import java.util.Objects;

public class Cover {
    private final String path;

    public Cover(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cover cover = (Cover) o;
        return Objects.equals(path, cover.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
