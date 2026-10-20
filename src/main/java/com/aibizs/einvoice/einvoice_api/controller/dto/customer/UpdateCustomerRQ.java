package com.aibizs.einvoice.einvoice_api.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Update Customer B2B and B2C Request Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateCustomerRQ {

    private String partyKind;
    private String customerName;
    private String tradeName;
    private String customerCode;

    private String taxId;
    private String registrationNumber;

    private String email;
    private String phone;
    private String website;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String countryCode;

    private String contactPerson;
    private String contactEmail;
    private String contactPhone;

    private String endpointScheme;
    private String endpointId;

    private Boolean isActive;
    private String notes;
}
