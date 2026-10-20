package com.aibizs.einvoice.einvoice_api.application.connector.dto;

import lombok.Data;

@Data
public class LineItemDTO {
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
}
