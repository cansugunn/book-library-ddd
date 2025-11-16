package com.finalproject.domain.entity;

import java.util.Objects;

public abstract class BaseEntity<ID> {
    private ID id;

    public BaseEntity(ID id) {
        this.id = id;
    }

    public abstract void validate();

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity<?> that = (BaseEntity<?>) obj;
        return id.equals(that.id);
    }
}
