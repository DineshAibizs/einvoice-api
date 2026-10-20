package com.aibizs.einvoice.einvoice_api.application.connector.dto;

public record Party(String endpointScheme, String endpointId, String name, String street, String city,
                    String postalCode, String countryCode, String vatId, String legalName, String companyId) {
}
