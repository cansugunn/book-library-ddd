package com.finalproject.application.ports.output.security;

public interface PasswordEncryptor {
    String encrypt(String password);
    String decrypt(String password);
}
