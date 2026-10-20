package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.Violation;

import java.util.List;

public record Validation(
        boolean passed,
        List<Violation> errors) {
}
