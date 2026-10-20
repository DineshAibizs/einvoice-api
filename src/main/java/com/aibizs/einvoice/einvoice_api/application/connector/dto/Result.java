package com.aibizs.einvoice.einvoice_api.application.connector.dto;

import java.util.List;

public record Result(boolean valid, List<Violation> errors) {
}
