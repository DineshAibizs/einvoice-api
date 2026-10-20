package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

public record ValidationFailedResponse(
        String transactionId,
        String status,
        Validation validation
) {}
