package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

public record TaxReporting(
        boolean tddSent,
        String tddInstanceId
) {}
