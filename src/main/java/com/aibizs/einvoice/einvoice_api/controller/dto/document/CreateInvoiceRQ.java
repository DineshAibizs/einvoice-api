package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote Request Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateInvoiceRQ {

    private String invoiceType;
    private String invoiceNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate; // YYYY-MM-DD HH:MM must NOT be future
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
    private List<LineItemRQ> lineItems;

}
