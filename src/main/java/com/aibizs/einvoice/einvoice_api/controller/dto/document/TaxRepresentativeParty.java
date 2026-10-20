package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record TaxRepresentativeParty(
        String name,
        String vatNumber,
        Address address
) {
}