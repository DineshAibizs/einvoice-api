package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record BillingReference(
        String invoiceId,
        String issueDate,
        String uuid,       // BTOM-031
        String reasonCode  // BTOM-032
) {}
