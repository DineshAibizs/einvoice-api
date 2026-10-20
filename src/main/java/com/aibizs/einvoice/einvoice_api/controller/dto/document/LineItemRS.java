package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote Response LineItem Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LineItemRS {
    private String id;
    private String unitPrice;
    private String taxRate;
    private String taxAmount;
    private String lineTotal;
    private String itemNetAmount;
    private String lineAllowanceAmount;
    private String lineAllowanceBaseAmount;
    private String lineChargeAmount;
    private String lineChargeBaseAmount;
    private String itemGrossPrice;
    private String itemPriceDiscount;
    private String lineAmountOmr;
    private String lineVatAmountOmr;
    private Integer lineNumber;
    private String itemDescription;
    private String itemCode;
    private String quantity;
    private String unitOfMeasure;
    private Map<String, Object> additionalData;
    private String invoiceLineIdentifier;
    private String invoiceLineNote;
    private String itemVatCategoryCode;
    private String itemTaxExemptionReasonCode;
    private String itemTaxExemptionReason;
    private Boolean isReverseCharge;
    private String lineAllowancePercentage;
    private String lineAllowanceReasonCode;
    private String lineAllowanceReason;
    private String lineChargePercentage;
    private String lineChargeReasonCode;
    private String lineChargeReason;
    private String itemName;
    private String itemType;
    private String itemBuyerIdentifier;
    private String itemStandardIdentifier;
    private String itemClassificationIdentifier;
    private String industrialClassificationCode;
    private String itemCountryOfOrigin;
    private String purchaseOrderLineReference;
    private String invoice;

}
