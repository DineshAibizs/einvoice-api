package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

public record ValidatedResponse(
        String transactionId,
        String status,
        String invoiceUuid,
        Validation validation
) {}
