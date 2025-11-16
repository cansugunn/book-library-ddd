package com.finalproject.domain.valueobject;

import java.util.Objects;

public class Year {
    private final int value;

    public Year(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Year)) return false;
        Year year = (Year) o;
        return value == year.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
