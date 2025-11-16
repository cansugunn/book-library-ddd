package com.finalproject.domain.entity;

import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.Website;

public class Author extends BaseEntity<AuthorId> {
    private String name;
    private String surname;
    private Website website;

    private Author(Builder builder) {
        super(builder.id);
        name = builder.name;
        surname = builder.surname;
        website = builder.website;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Override
    public void validate() {

    }

    public static final class Builder {
        private AuthorId id;
        private String name;
        private String surname;
        private Website website;

        public Builder() {
        }

        public Builder id(AuthorId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder surname(String val) {
            surname = val;
            return this;
        }

        public Builder website(Website val) {
            website = val;
            return this;
        }

        public Author build() {
            return new Author(this);
        }
    }
}
