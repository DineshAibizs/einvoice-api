package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreditNoteDocument(

        DocumentExtensions extensions,

        String creditNoteNumber,
        String issueDate,
        String issueTime,

        String creditNoteTypeCode,

        @Schema(description = "BTOM-001 on CreditNoteTypeCode/@name")
        String transactionType,

        String creditNoteTypeCodeName,

        String currency,

        String buyerReference,
        String note,

        Party seller,
        Party buyer,

        BillingReference billingReference,

        InvoicePeriod invoicePeriod,
        OrderReference orderReference,

        List<TaxTotal> taxTotals,

        MonetaryTotal monetaryTotal,

        List<InvoiceLineItem> lines

) {

    public CreditNoteDocument {

        // issueTime: str = "12:00:00"
        if (issueTime == null) {
            issueTime = "12:00:00";
        }

        // creditNoteTypeCode: str = "381"
        if (creditNoteTypeCode == null) {
            creditNoteTypeCode = "381";
        }

        // transactionType default
        if (transactionType == null) {
            transactionType = "10000000000000000000";
        }

        // currency: str = "OMR"
        if (currency == null) {
            currency = "OMR";
        }

        // Equivalent of Python @model_validator
        if (creditNoteTypeCodeName == null || creditNoteTypeCodeName.isBlank()) {
            creditNoteTypeCodeName = transactionType;
        }
    }
}