package com.finalproject.infrastructure.security;

import com.finalproject.application.ports.output.security.PasswordEncryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CypherPasswordEncryptor implements PasswordEncryptor {
    private static final String SECRET_KEY = "L0ZRUIWjsQgPf74IEAHO5w==";
    private static final String ALGORITHM = "AES";

    @Override
    public String encrypt(String password) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), ALGORITHM);
            Cipher encryptCipher = Cipher.getInstance(ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = encryptCipher.doFinal(password.getBytes());
            String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);
            return encryptedBase64;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String decrypt(String password) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), ALGORITHM);
            Cipher decryptCipher = Cipher.getInstance(ALGORITHM);
            decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = decryptCipher.doFinal(Base64.getDecoder().decode(password));
            return new String(decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
