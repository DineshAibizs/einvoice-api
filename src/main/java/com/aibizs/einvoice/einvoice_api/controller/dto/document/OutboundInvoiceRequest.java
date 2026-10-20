package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import lombok.Data;


public record OutboundInvoiceRequest(
        RequestMetadata metadata,
        ParticipantRef sender,
        ParticipantRef receiver,
        InvoiceDocument document,
        SubmitOptions options
) {
    // Default constructor handling default options initialization
    public OutboundInvoiceRequest {
        if (options == null) {
            options = new SubmitOptions(false, false, false, false);
        }
    }
}