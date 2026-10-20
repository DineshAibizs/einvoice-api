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
@Schema(description = "Create Invoice/Creditnote Response OmanQR Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OmanQRRS {

    private String format;
    private String version;
    private String invoiceType;
    private String sellerUuid;
    private String publicPdfUrl;
    private String publicViewUrl;
    private String scanUrl;
    private String imageDataUrl;
    private String tlvBase64;
    private Integer maxBase64Length;
    private List<FieldRS> fields;
    private String notes;

}
