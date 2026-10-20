package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Invoice Summary Response Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceSummaryRS {

    private String invoiceNumber;
    private String trackingNumber;
    private String processedReferenceNumber;
    private String governmentSuccessCode;
    private String invoiceTypeName;
    private String invoiceTypeCode;
    private String invoiceDate;
    private String dueDate;
    private String status;
    private String currency;
    private String complianceProfile;
    private String customerName;
    private String buyerTradingName;
    private String buyerTaxId;
    private String buyerVatIdentifier;
    private String buyerEmail;
    private String sellerName;
    private Double subtotal;
    private Double taxAmount;
    private Double totalAmount;
}
