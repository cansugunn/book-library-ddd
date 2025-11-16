package com.finalproject.infrastructure.security;

public class Main {
    public static void main(String[] args) {
        String x = """
                INSERT INTO userinfo (username, password, type)
                VALUES ('alice', 'password123', 1);
                INSERT INTO userinfo (username, password, type)
                VALUES ('bob', 'securepass', 2);
                INSERT INTO userinfo (username, password, type)
                VALUES ('charlie', 'charliepwd', 1);
                INSERT INTO userinfo (username, password, type)
                VALUES ('dave', 'dave123', 2);
                INSERT INTO userinfo (username, password, type)
                VALUES ('eve', 'evepw', 1);
                """;


        CypherPasswordEncryptor cypherPasswordEncryptor =
                new CypherPasswordEncryptor();
        System.out.printf("%s\n%s\n%s\n%s\n%s",
                cypherPasswordEncryptor.encrypt("yasar1"),
                cypherPasswordEncryptor.encrypt("yasar2"),
                cypherPasswordEncryptor.encrypt("yasar3"),
                cypherPasswordEncryptor.encrypt("yasar4"),
                cypherPasswordEncryptor.encrypt("yasar5"));
    }
}
