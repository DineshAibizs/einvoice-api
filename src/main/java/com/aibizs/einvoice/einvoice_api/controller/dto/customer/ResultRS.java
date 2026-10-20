package com.aibizs.einvoice.einvoice_api.controller.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "List Customer Result Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResultRS {

    private String id;
    private String company;
    private String companyName;
    private String customerName;
    private String tradeName;
    private String customerCode;
    private String erpUuid;
    private String partyKind;
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
    private String endpointId;
    private String endpointScheme;
    private Boolean isActive;
    private String notes;
    private String peppolNetworkStatus;
    private LocalDateTime peppolCheckedAt;
    private String createdBy;
    private String createdByName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
