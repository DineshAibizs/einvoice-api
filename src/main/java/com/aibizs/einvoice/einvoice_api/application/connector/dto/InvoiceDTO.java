package com.aibizs.einvoice.einvoice_api.application.connector.dto;

import com.aibizs.einvoice.einvoice_api.controller.dto.document.LineItemRQ;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceDTO {
    private String invoiceType;
    private String invoiceNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate; // YYYY-MM-DD HH:MM must NOT be future

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate; // YYYY-MM-DD HH:MM
    private String currency;
    private String vatAccountingCurrencyCode;
    private String currencyExchangeRate;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String customerErpUuid;
    private String customerCode;

    private String subTotal;
    private String taxAmount;
    private String totalAmount;

    private String complianceProfile;
    private String documentSide;
    private String invoiceTypeCode;
    private String invoiceTransactionType;

    private String specificationIdentifier;
    private String businessProcessType;
    private String precedingInvoiceNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate precedingInvoiceIssueDate;
    private String precedingInvoiceUuid;
    private String creditDebitNoteReasonCode;
    private List<LineItemDTO> lineItems;
}
