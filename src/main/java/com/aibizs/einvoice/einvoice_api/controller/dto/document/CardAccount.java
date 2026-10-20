package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record CardAccount(
        String primaryAccountNumberId,
        String networkId,
        String holderName
) {
}
