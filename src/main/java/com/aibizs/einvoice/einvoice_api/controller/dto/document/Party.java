package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public record Party(
        ParticipantRef endpoint,
        String name,
        String vatNumber,
        String tradeName,
        String companyId,
        String companyLegalForm,
        String partyIdentification,
        String additionalAccountId,
        Address address,
        Contact contact
) {}
