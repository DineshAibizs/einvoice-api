package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

public record SubmitResponse(
        String transactionId,
        String correlationId,
        String status,
        String documentType,
        String invoiceUuid,
        String sbdInstanceId,
        Peppol peppol,
        TaxReporting taxReporting,
        Validation validation
) {
}
