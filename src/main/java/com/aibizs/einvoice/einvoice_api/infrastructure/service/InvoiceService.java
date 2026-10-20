package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.controller.dto.document.CreateInvoiceRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.CreateInvoiceRS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceService {

    public CreateInvoiceRS create(CreateInvoiceRQ createInvoiceRQ) {

        log.debug("createInvoiceRQ-->{}", createInvoiceRQ);
        CreateInvoiceRS createInvoiceRS = new CreateInvoiceRS();

        return createInvoiceRS;
    }

}
