package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record PaymentTerms(
        String paymentMeansId,
        String note,
        Double amount,
        String currency,
        String installmentDueDate
) {

    public PaymentTerms {
        if (currency == null) {
            currency = "OMR";
        }
    }
}
