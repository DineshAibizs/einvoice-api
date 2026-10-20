package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;

public record Delivery(
        String actualDeliveryDate,
        String locationId,
        String locationSchemeId,
        Address address,
        String deliveryPartyName,

        @Schema(description = "Incoterms e.g. CIF")
        String deliveryTermsId,

        String shipmentId,
        String shipmentDeliveryDate
) {

    public Delivery {
        if (locationSchemeId == null) {
            locationSchemeId = "0235";
        }
    }
}