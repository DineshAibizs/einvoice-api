package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record AdditionalDocumentReference(
        String id,
        String uuid,
        String documentTypeCode,
        String documentDescription,
        EmbeddedAttachment attachment
) {
}