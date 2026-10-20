package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.util.List;

public record DocumentExtensions(
        DocumentReference receiptDocumentReference,
        DocumentReference originatorDocumentReference,
        DocumentReference contractDocumentReference,
        DocumentReference despatchDocumentReference,
        DocumentReference projectReference,
        List<AdditionalDocumentReference> additionalDocumentReferences,
        PayeeParty payeeParty,
        TaxRepresentativeParty taxRepresentativeParty,
        Delivery delivery,
        List<PaymentMeans> paymentMeans,
        List<PaymentTerms> paymentTerms,
        List<PrepaidPayment> prepaidPayments,
        List<AllowanceCharge> allowanceCharges
) {}