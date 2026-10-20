package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record ParticipantRef(
        String schemeId,      // e.g., "0248"
        String participantId  // e.g., "OM110048187X"
) {}