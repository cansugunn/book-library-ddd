package com.finalproject.application.dto.page;

public record PageQuery(int page, int size) {
    public PageQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
    }
}

