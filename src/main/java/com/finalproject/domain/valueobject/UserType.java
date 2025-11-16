package com.finalproject.domain.valueobject;

public enum UserType {
    ADMIN(1), NORMAL(2);

    private final int ordinaryValue;
    private static UserType[] CACHED_VALUES = UserType.values();

    private UserType(int ordinaryValue) {
        this.ordinaryValue = ordinaryValue;
    }

    public static UserType of(int ordinaryValue) {
        for (UserType type : CACHED_VALUES) {
            if (type.ordinaryValue == ordinaryValue) {
                return type;
            }
        }
        return null;
    }
}
