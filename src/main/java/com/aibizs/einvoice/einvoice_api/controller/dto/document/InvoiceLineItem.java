package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.util.List;

public  record InvoiceLineItem(
        String id,
        Double quantity,
        String unitCode,
        Double lineExtensionAmount,
        String currency,
        String note,
        InvoicePeriod invoicePeriod,
        OrderLineReference orderLineReference,
        DespatchLineReference despatchLineReference,
        List<AllowanceCharge> allowanceCharges,
        InvoiceLineItemDetail item,
        PriceDetail price,
        TaxSubtotal tax
) {
    public InvoiceLineItem {
        if (unitCode == null) unitCode = "H87";
        if (currency == null) currency = "OMR";

        if (price != null && "EA".equals(price.unitCode()) && !"EA".equals(unitCode)) {
            price = new PriceDetail(
                    price.amount(),
                    price.currency(),
                    price.baseQuantity(),
                    unitCode,
                    price.allowanceCharges()
            );
        }
    }
}

