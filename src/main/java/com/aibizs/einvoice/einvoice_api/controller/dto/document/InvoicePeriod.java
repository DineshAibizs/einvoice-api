package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record InvoicePeriod(
        String startDate,
        String endDate,
        String descriptionCode
) {}