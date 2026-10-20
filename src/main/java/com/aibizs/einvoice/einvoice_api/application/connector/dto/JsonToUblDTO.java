package com.aibizs.einvoice.einvoice_api.application.connector.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JsonToUblDTO {
    private String traceId;
    private Party customerParty;
    private Party supplierParty;
    private Payment payment;
    private String buyerReference;
    private InvoiceDTO invoiceDTO;
}
