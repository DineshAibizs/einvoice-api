package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.util.List;

public record PriceDetail(
        double amount,
        String currency,
        double baseQuantity,
        String unitCode,
        List<AllowanceCharge> allowanceCharges
) {
    public PriceDetail {
        if (currency == null) currency = "OMR";
        if (baseQuantity == 0) baseQuantity = 1.0;
        if (unitCode == null) unitCode = "EA";
    }
}

