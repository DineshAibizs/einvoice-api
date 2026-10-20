package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote - Line Item Request Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateLineItemRQ {

    private int lineNumber;
    private String itemDescription;
    private String quantity;
    private String unitPrice;
    private String lineAllowanceAmount;
    private String lineAllowanceReason;
    private String taxRate;
    private String taxAmount;
    private String lineTotal;
    private String invoiceLineIdentifier;
    private String itemNetAmount;
    private String itemType;
    private String itemVatCategoryCode;
    private String unitOfMeasure;
    private String industrialClassificationCode;
    private String itemTaxExemptionReasonCode;
    private String itemTaxExemptionReason;
    private Boolean isReverseCharge;


}
