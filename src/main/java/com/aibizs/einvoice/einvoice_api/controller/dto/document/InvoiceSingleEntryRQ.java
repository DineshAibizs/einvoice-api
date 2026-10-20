package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote Single Entry Request Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceSingleEntryRQ {

    private String invoiceType;
    private String invoiceNumber;
    private String invoiceDate;
    private String currency;
    private String vatAccountingCurrencyCode;
    private String currencyExchangeRate;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String subtotal;
    private String taxAmount;
    private String totalAmount;
    private String complianceProfile;
    private String documentSide;
    private String invoiceTypeCode;
    private String invoiceTransactionType;
    private String specificationIdentifier;
    private String businessProcessType;
    private String customerCode;
    private String customerErpUuid;

    private String buyerElectronicAddressScheme;
    private String buyerElectronicAddress;
    private String buyerVatIdentifier;
    private String buyerVatScheme;
    private String buyerLegalRegistrationIdentifier;
    private String buyerName;
    private String buyerTradingName;
    private String buyerCountrySubdivisionCode;
    private String buyerAddressLine1;
    private String buyerCity;
    private String buyerPostalCode;
    private String buyerCountryCode;
    private String buyerContactPoint;
    private String buyerContactTelephoneNumber;
    private String buyerContactEmailAddress;
    private String buyerIdentifier;

    private List<LineItemRQ> lineItems;
}
