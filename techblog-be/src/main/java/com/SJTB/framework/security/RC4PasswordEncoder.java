package com.SJTB.framework.security;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

@Component
public class RC4PasswordEncoder implements PasswordEncoder {

    private static final String RC4_ALGORITHM = "RC4";
    private static final String ENCRYPTION_KEY = "0x010000002F6BC673E04B0820FD92A74CC8CF7B3EC27699FD8C54B3E7";

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), RC4_ALGORITHM);
            Cipher cipher = Cipher.getInstance(RC4_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedPasswordBytes = cipher.doFinal(rawPassword.toString().getBytes());
            return new String(Hex.encode(encryptedPasswordBytes));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error encoding password", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), RC4_ALGORITHM);
            Cipher cipher = Cipher.getInstance(RC4_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedPasswordBytes = Hex.decode(encodedPassword);
            byte[] decryptedPasswordBytes = cipher.doFinal(encryptedPasswordBytes);
            String decryptedPassword = new String(decryptedPasswordBytes);
            return rawPassword.toString().equals(decryptedPassword);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error decoding password", e);
        }
    }
}
