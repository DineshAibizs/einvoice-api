package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.controller.SenderAccessDeniedException;
import com.aibizs.einvoice.einvoice_api.controller.dto.auth.ApiKeyContext;

import com.aibizs.einvoice.einvoice_api.controller.util.KeyHasher;
import com.aibizs.einvoice.einvoice_api.domain.ApiKey;
import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import com.aibizs.einvoice.einvoice_api.infrastructure.repository.ApiKeyRepository;
import com.aibizs.einvoice.einvoice_api.infrastructure.repository.RegisteredPartyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private static final String KEY_PREFIX = "ota_";
    private static final int LOOKUP_PREFIX_LEN = 12;

    @Value("${app.api-key:}")
    private String legacyApiKey;

    private final ApiKeyRepository apiKeyRepository;
    private final RegisteredPartyRepository partyRepository;
    private final KeyHasher keyHasher;

    public ApiKeyContext verifyKey(String rawKey) {

        if (!StringUtils.hasText(rawKey)) {
            return null;
        }

        if (StringUtils.hasText(legacyApiKey) && !"change-me-in-production" .equals(legacyApiKey)) {

            if (MessageDigest.isEqual(rawKey.getBytes(StandardCharsets.UTF_8), legacyApiKey.getBytes(StandardCharsets.UTF_8))) {
                return new ApiKeyContext(0L, "Legacy environment key", null, true);
            }
        }

        if (!rawKey.startsWith(KEY_PREFIX) || rawKey.length() < LOOKUP_PREFIX_LEN) {
            return null;
        }

        String prefix = rawKey.substring(0, LOOKUP_PREFIX_LEN);
        String expectedHash = keyHasher.hashKey(rawKey);

        List<ApiKey> candidates = apiKeyRepository.findByIsActiveTrueAndKeyPrefix(prefix);

        for (ApiKey row : candidates) {

            byte[] savedHashBytes = HexFormat.of().parseHex(row.getKeyHash());
            byte[] expectedHashBytes = HexFormat.of().parseHex(expectedHash);

            if (MessageDigest.isEqual(savedHashBytes, expectedHashBytes)) {

                row.setLastUsedAt(LocalDateTime.now());
                apiKeyRepository.save(row);

                RegisteredParty party = null;
                if (row.getParty() != null) {
                    party = row.getParty();
                }

                return new ApiKeyContext(row.getId(), row.getLabel(), party, false);
            }
        }

        return null;
    }

    private void assertSenderAllowed(ApiKeyContext ctx, String senderScheme, String senderId) {
        if (ctx.legacy() || ctx.party() == null) {
            return;
        }

        var party = ctx.party();
        if (!party.getSchemeId().equals(senderScheme) || !party.getParticipantId().equals(senderId)) {
            throw new IllegalArgumentException(String.format(
                    "API key is restricted to party %s; request sender is %s:%s",
                    party.getPeppolId(), senderScheme, senderId
            ));
        }
    }

    public void enforceSender(ApiKeyContext ctx, String senderScheme, String senderId) {
        try {
            assertSenderAllowed(ctx, senderScheme, senderId);
        } catch (IllegalArgumentException exc) {
            throw new SenderAccessDeniedException(exc.getMessage());
        }
    }

}