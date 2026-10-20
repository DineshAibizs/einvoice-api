package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote Response CompanyDetails Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanyDetailsRS {

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("registration_number")
    private String registrationNumber;

    @JsonProperty("tax_id")
    private String taxId;

    @JsonProperty("address_line1")
    private String addressLine1;

    @JsonProperty("address_line2")
    private String addressLine2;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("business_type")
    private String businessType;

    @JsonProperty("registration_name")
    private String registrationName;

    @JsonProperty("vat_id")
    private String vatId;

    @JsonProperty("tax_registration_id")
    private String taxRegistrationId;

    @JsonProperty("tax_scheme")
    private String taxScheme;

    @JsonProperty("legal_registration_id")
    private String legalRegistrationId;

    @JsonProperty("contact_telephone")
    private String contactTelephone;

    @JsonProperty("contact_email")
    private String contactEmail;

    @JsonProperty("endpoint_id")
    private String endpointId;

    @JsonProperty("endpoint_scheme")
    private String endpointScheme;

    @JsonProperty("logo_url")
    private String logoUrl;
}
