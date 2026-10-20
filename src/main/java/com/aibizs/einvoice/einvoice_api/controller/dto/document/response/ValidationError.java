package com.aibizs.einvoice.einvoice_api.controller.dto.document.response;

public record ValidationError(String ruleId, String message, String location) {
}
