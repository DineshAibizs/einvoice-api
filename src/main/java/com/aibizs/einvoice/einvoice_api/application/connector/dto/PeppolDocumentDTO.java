package com.aibizs.einvoice.einvoice_api.application.connector.dto;

import com.aibizs.einvoice.einvoice_api.controller.dto.document.DocumentType;

public record PeppolDocumentDTO(
        DocumentType documentType,
        String sender,
        String receiver,
        String instanceId,
        byte[] xmlBytes
) {
}
