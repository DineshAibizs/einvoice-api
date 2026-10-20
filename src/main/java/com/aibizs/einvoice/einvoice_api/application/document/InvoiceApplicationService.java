package com.aibizs.einvoice.einvoice_api.application.document;

import com.aibizs.einvoice.einvoice_api.application.connector.UblConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.ValidationConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.InvoiceDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.JsonToUblDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.LineItemDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.Party;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.CreateInvoiceRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.CreateInvoiceRS;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.LineItemRQ;
import com.aibizs.einvoice.einvoice_api.controller.util.CommonUtil;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.InvoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InvoiceApplicationService {

    private final UblConnector ublConnector;
    private final ValidationConnector validationConnector;

    public CreateInvoiceRS processInvoice(CreateInvoiceRQ createInvoiceRQ) {

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setLineItems(new ArrayList<>());
        CommonUtil.deepCopy(createInvoiceRQ, invoiceDTO);

        Party customerParty = new Party("testCusEPScheme", "testCusEPId", "testCusName", "testCusStreet", "testCusCity", "testCusPostalCode", "testCusCountryCode", "testCusVatId", "testCusLegalName", "testCusCompanyId");
        Party supplierParty = new Party("testSupEPScheme", "testSupEPId", "testSupName", "testSupStreet", "testSupCity", "testSupPostalCode", "testSupCountryCode", "testSupVatId", "testSupLegalName", "testSupCompanyId");

        JsonToUblDTO jsonToUblDTO = JsonToUblDTO.builder()
                .invoiceDTO(invoiceDTO)
                .customerParty(customerParty)
                .supplierParty(supplierParty)
                .traceId(MDC.get(CommonUtil.TRACE_ID))
                .build();


        String xml = ublConnector.convertToJsonToXml(jsonToUblDTO);
        log.info("xml-->{}", xml);
//        String validationResponse = validationConnector.validateXml(xml);
//        log.info("validationResponse-->{}", validationResponse);
        return new CreateInvoiceRS();
    }


}
