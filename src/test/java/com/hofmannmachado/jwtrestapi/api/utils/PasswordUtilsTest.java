package com.hofmannmachado.jwtrestapi.api.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordUtilsTest {

    private static final String SENHA = "123456";
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

    @Test
    public void testaSenhaNula() throws Exception {
        assertNull(PasswordUtils.gerarBCrypt(null));
    }

    @Test
    public void testaSenha() throws Exception {
        String hash = (PasswordUtils.gerarBCrypt(SENHA));

        assertTrue(bCryptEncoder.matches(SENHA, hash));
    }
}
