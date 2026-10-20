package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public  record DocumentReference(
        String id,
        String issueDate,
        String documentTypeCode,
        String documentDescription
) {
}
