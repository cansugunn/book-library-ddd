package com.finalproject.application.dto;

public class FindAuthorResponse {
    private final Integer id;
    private final String name;
    private final String surname;
    private final String websiteUrl;

    private FindAuthorResponse(Builder builder) {
        id = builder.id;
        name = builder.name;
        surname = builder.surname;
        websiteUrl = builder.websiteUrl;
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private String surname;
        private String websiteUrl;

        public Builder() {
        }

        public Builder id(Integer val) {
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

        public Builder websiteUrl(String val) {
            websiteUrl = val;
            return this;
        }

        public FindAuthorResponse build() {
            return new FindAuthorResponse(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    @Override
    public String toString() {
        return "DisplayAuthorResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                '}';
    }
}
