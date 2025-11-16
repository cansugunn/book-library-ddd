package com.finalproject.domain.entity;

import com.finalproject.domain.valueobject.CommentId;

public class Comment extends BaseEntity<CommentId> {
    private final String value;

    public Comment(CommentId id, String value) {
        super(id);
        this.value = value;
    }

    private Comment(Builder builder) {
        super(builder.id);
        value = builder.value;
    }


    @Override
    public void validate() {

    }

    public String getValue() {
        return value;
    }

    public static final class Builder {
        private CommentId id;
        private String value;

        public Builder() {
        }

        public Builder id(CommentId val) {
            id = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
