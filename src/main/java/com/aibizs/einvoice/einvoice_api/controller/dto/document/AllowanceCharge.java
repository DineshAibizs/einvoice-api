package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record AllowanceCharge(
        boolean chargeIndicator, // false = allowance, true = charge
        String reasonCode,
        String reason,
        Double multiplierFactor,
        double amount,
        String currency,
        Double baseAmount,
        TaxCategory taxCategory
) {
    public AllowanceCharge {
        if (currency == null) currency = "OMR";
    }
}

