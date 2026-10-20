package com.aibizs.einvoice.einvoice_api.controller.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class KeyHasher {

    private final String pepper;

    public KeyHasher(
            @Value("${app.security.api-key-pepper:}") String apiKeyPepper,
            @Value("${app.security.admin-secret-key:}") String adminSecretKey) {
        this.pepper = resolvePepper(apiKeyPepper, adminSecretKey);
    }

    private static String resolvePepper(String apiKeyPepper, String adminSecretKey) {
        String value = !isEmpty(apiKeyPepper) ? apiKeyPepper : !isEmpty(adminSecretKey) ? adminSecretKey : "ota";
        return value.strip();
    }

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public String hashKey(String rawKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest((pepper + ":" + rawKey).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
