package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Address {

    private String streetName;

    private String cityName;

    private String postalZone;

    private String countryCode = "OM";

    private String additionalStreetName;

    private String countrySubentity = "CS";

    @Schema(example = "SHRFZ")
    private String countrySubentityCode;

    private String addressLine;
}