package com.finalproject.infrastructure.spring.security.passwordencoder;

import com.finalproject.application.ports.output.security.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SpringPasswordEncoderAdapter implements PasswordEncoder {
    private final PasswordEncryptor passwordEncryptor;

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncryptor.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(passwordEncryptor.encrypt(rawPassword.toString()), encodedPassword);
    }
}