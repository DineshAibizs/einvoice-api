package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Update Invoice/Creditnote Request Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateInvoiceRQ {

    private String invoiceNumber;
    private String invoiceDate;
    private String dueDate;
    private String customerErpUuid;
    private String customerCode;
    private String currency;
    private String vatAccountingCurrencyCode;
    private String currencyExchangeRate;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String subtotal;
    private String taxAmount;
    private String totalAmount;
    private String invoiceTypeCode;
    private String complianceProfile;
    private String taxMechanism;
    private Boolean isCreditNote;
    private Boolean isSelfBilled;
    private String specificationIdentifier;
    private String businessProcessType;
    private String sellerIdentifier;
    private String sellerAddressLine1;
    private String sellerCity;
    private String sellerPostalCode;
    private String sellerCountryCode;
    private String buyerIdentifier;
    private String buyerTradingName;
    private String buyerAddressLine1;
    private String buyerCity;
    private String buyerPostalCode;
    private String buyerCountryCode;
    private String invoiceNote;
    private List<UpdateLineItemRQ> lineItems;
}
