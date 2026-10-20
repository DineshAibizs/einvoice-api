package com.aibizs.einvoice.einvoice_api.controller.dto.document;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record TaxCategory(

        @Schema(description = "UNCL5305 e.g. S, Z, E")
        String id,

        BigDecimal percent,

        String taxSchemeId,

        String exemptionReasonCode,

        String exemptionReason

) {

    public TaxCategory {
        if (taxSchemeId == null) {
            taxSchemeId = "VAT";
        }
    }
}