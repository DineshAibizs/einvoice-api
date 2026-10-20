package com.aibizs.einvoice.einvoice_api.controller.dto.auth;

import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;

public record ApiKeyContext(
        Long keyId,
        String label,
        RegisteredParty party,
        boolean legacy
) {
}