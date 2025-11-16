package com.finalproject.domain.valueobject;

public enum Read {
    READ(1), NOT_READ(2), WISH_TO_BE_READ(3);

    private final int ordinaryValue;

    private Read(int ordinaryValue) {
        this.ordinaryValue = ordinaryValue;
    }

    private static final Read[] CACHED_VALUES = Read.values();

    public static Read of(int ordinaryValue) {
        for (Read read : CACHED_VALUES) {
            if (read.getOrdinaryValue() == ordinaryValue) {
                return read;
            }
        }
        return null;
    }

    public int getOrdinaryValue() {
        return ordinaryValue;
    }
}
