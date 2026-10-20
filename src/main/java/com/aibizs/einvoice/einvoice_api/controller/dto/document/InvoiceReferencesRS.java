package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Get Invoice References Response Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceReferencesRS {

    private String invoiceNumber;
    private String trackingNumber;
    private String processedReferenceNumber;
    private String governmentSuccessCode;
    private String processed;

    private InvoiceReferencesQrCodeRS qrCode;

    private LocalDateTime trackingNumberSetAt;
    private LocalDateTime processedReferenceNumberSetAt;
    private LocalDateTime lastUpdated;


}
