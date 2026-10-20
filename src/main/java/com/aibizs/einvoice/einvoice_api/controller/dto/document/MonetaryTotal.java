package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public  record MonetaryTotal(
        Double lineExtensionAmount,   // Double instead of double allows null-checking
        Double taxExclusiveAmount,
        Double taxInclusiveAmount,
        Double payableAmount,
        String currency,
        Double allowanceTotalAmount,
        Double chargeTotalAmount,
        Double prepaidAmount,
        Double payableRoundingAmount
) {
    public MonetaryTotal {
        if (currency == null) currency = "OMR";
    }
}
