package com.aibizs.einvoice.einvoice_api.application.connector;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.PeppolDocumentDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.Result;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.DocumentType;
import com.aibizs.einvoice.einvoice_api.controller.util.CommonUtil;
import com.aibizs.einvoice.einvoice_api.controller.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.w3c.dom.Document;

@RequiredArgsConstructor
@Slf4j
@Service
public class PeppolConnector {

    private final XmlUtil xmlUtil;
    @Value("${einvoice.peppol-ap.service.validate.xml}")
    private String getPeppolEndPoint;

    private final RestClient peppolApServiceClient;

    public String connectPeppolAp(DocumentType docType, String sender, String receiver, String txnId, Document document) {

        byte[] xmlBytes = xmlUtil.documentToBytes(document);

        PeppolDocumentDTO peppolDocumentDTO = new PeppolDocumentDTO(docType, sender, receiver, txnId, xmlBytes);

        return peppolApServiceClient.post()
                .uri(getPeppolEndPoint)
                .header("X-Trace-Id", MDC.get(CommonUtil.TRACE_ID))
                .contentType(MediaType.APPLICATION_JSON) // Sending JSON
                .accept(MediaType.APPLICATION_JSON)       // Expecting XML
                .body(peppolDocumentDTO)                 // Spring automatically serializes to JSON
                .retrieve()
                .body(String.class);                      // Captures raw XML string response
    }
}
