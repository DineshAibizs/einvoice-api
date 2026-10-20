package com.aibizs.einvoice.einvoice_api.controller.dto.document;

public enum DocumentType {

    PINT_OM_INVOICE("pint_om_invoice"),
    PINT_OM_CREDIT_NOTE("pint_om_credit_note"),
    PINT_OM_SELF_INVOICE("pint_om_self_billing_invoice"),
    PINT_OM_SELF_CREDIT_NOTE("pint_om_self_billing_credit_note");

    private final String type;

    DocumentType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
