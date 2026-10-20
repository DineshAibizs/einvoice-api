package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.util.List;

public record InvoiceDocument(
        DocumentExtensions extensions,
        String invoiceNumber,
        String issueDate,
        String issueTime,
        String dueDate,
        String invoiceTypeCode,
        String transactionType,
        String invoiceTypeCodeName,
        String currency,
        String taxPointDate,
        String buyerReference,
        String note,
        String accountingCost,
        Party seller,
        Party buyer,
        InvoicePeriod invoicePeriod,
        OrderReference orderReference,
        BillingReference billingReference,
        List<TaxTotal> taxTotals,
        MonetaryTotal monetaryTotal,
        List<InvoiceLineItem> lines
) {
    // Compact constructor to mirror Pydantic's @model_validator(mode="after")
    public InvoiceDocument {
        if (issueTime == null) issueTime = "12:00:00";
        if (invoiceTypeCode == null) invoiceTypeCode = "380";
        if (transactionType == null) transactionType = "10000000000000000000";
        if (currency == null) currency = "OMR";

        // Sync invoiceTypeCodeName with transactionType if omitted
        if (invoiceTypeCodeName == null || invoiceTypeCodeName.isBlank()) {
            invoiceTypeCodeName = transactionType;
        }
    }
}