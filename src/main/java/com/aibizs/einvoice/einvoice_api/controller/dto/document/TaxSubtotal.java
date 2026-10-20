package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record TaxSubtotal(
        double taxableAmount,
        double taxAmount,
        String currency,
        TaxCategory taxCategory
) {
    public TaxSubtotal {
        if (currency == null) currency = "OMR";
    }
}
