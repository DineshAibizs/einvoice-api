package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record PrepaidPayment(
        String id,
        Double paidAmount,
        String currency,
        String receivedDate,
        String instructionId
) {

    public PrepaidPayment {
        if (currency == null) {
            currency = "OMR";
        }
    }
}
