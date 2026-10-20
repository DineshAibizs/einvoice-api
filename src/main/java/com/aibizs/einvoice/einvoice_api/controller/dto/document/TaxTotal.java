package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.math.BigDecimal;
import java.util.List;

public record TaxTotal(
        BigDecimal taxAmount,
        String currency,
        Boolean taxIncludedIndicator,
        List<TaxSubtotal> taxSubtotals
) {

    public TaxTotal {
        if (currency == null) {
            currency = "OMR";
        }
    }
}
