package com.finalproject.domain.valueobject;

import java.util.Date;

public class ReleaseDate {
    private final Date date;

    public ReleaseDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
