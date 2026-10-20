package com.aibizs.einvoice.einvoice_api.application.connector;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.OxalisRequestDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.PeppolDocumentDTO;
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
public class OxalisConnector {

    private final XmlUtil xmlUtil;
    @Value("${einvoice.oxalis-connector.service.send}")
    private String getOxalisEndPoint;

    private final RestClient oxalisApServiceClient;

    public boolean connectPeppolAp(OxalisRequestDTO oxalisRequestDTO) {

        // Sending JSON
        // Expecting XML
        // Spring automatically serializes to JSON
        return Boolean.TRUE.equals(oxalisApServiceClient.post()
                .uri(getOxalisEndPoint)
                .header("X-Trace-Id", MDC.get(CommonUtil.TRACE_ID))
                .contentType(MediaType.APPLICATION_JSON) // Sending JSON
                .accept(MediaType.APPLICATION_JSON)       // Expecting XML
                .body(oxalisRequestDTO)                 // Spring automatically serializes to JSON
                .retrieve()
                .body(Boolean.class));                      // Captures raw XML string response
    }
}
