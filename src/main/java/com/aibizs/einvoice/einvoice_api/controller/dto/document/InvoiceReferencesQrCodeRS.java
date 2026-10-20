package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Get Invoice References QR Code Response Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceReferencesQrCodeRS {

    private String format;
    private String tlvBase64;
    private String imageDataUrl;
    private String scanUrl;
    private String publicViewUrl;
    private String publicPdfUrl;
    private String sellerUuid;
    private String invoiceType;
    private String version;
    private String notes;


}
