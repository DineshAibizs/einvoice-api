package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;

public record FinancialAccount(
        String id,

        @Schema(example = "IBAN")
        String schemeId,

        String name,
        String branchId,
        Address branchAddress
) {

    public FinancialAccount {
        if (schemeId == null) {
            schemeId = "IBAN";
        }
    }
}