package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@Schema(description = "Payload TDD Response Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayloadTDDRS {

    private BuyerTDDRS buyer;
    private List<LineTDDRS> lines;
    private SellerTDDRS seller;
    private InvoiceTDDRS invoice;
    private String version;
    private String standard;
    private String generatedAt;
}
