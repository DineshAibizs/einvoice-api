package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record SubmitOptions(
        Boolean  validateOnly,
        Boolean  skipTdd,
        Boolean  supplyUuid,
        Boolean  asyncSubmit
) {
    // Default values constructor
    public SubmitOptions() {
        this(false, false, false, false);
    }
}