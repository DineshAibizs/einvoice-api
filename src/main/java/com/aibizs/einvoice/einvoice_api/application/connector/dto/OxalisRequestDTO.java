package com.aibizs.einvoice.einvoice_api.application.connector.dto;

public record OxalisRequestDTO(String xmlPath, String transactionId, String senderSchemeId, String senderParticipantId, boolean skipTdd) {
}