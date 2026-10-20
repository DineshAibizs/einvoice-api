package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record RequestMetadata(
        String correlationId,
        String idempotencyKey
) {}