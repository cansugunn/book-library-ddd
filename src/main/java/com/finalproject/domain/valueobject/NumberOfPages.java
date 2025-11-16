package com.finalproject.domain.valueobject;

import java.util.Objects;

public class NumberOfPages {
    private final int value;

    public NumberOfPages(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfPages that = (NumberOfPages) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
