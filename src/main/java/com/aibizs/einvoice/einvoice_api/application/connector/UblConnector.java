package com.aibizs.einvoice.einvoice_api.application.connector;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.JsonToUblDTO;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.InvoiceDocument;
import com.aibizs.einvoice.einvoice_api.controller.util.CommonUtil;

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
public class UblConnector {

    @Value("${einvoice.ubl.service.convert.ubl}")
    private String getXmlEndPoint;

    private final RestClient ublServiceClient;

    public String convertToJsonToXml(JsonToUblDTO jsonToUblDTO) {
        return ublServiceClient.post()
                .uri(getXmlEndPoint)
                .header("X-Trace-Id", MDC.get(CommonUtil.TRACE_ID))
                .contentType(MediaType.APPLICATION_JSON) // Sending JSON
                .accept(MediaType.APPLICATION_XML)       // Expecting XML
                .body(jsonToUblDTO)                 // Spring automatically serializes to JSON
                .retrieve()
                .body(String.class);                      // Captures raw XML string response
    }

    public String convertToJsonToXml(InvoiceDocument document, String docType) {
        return ublServiceClient.post()
                .uri(getXmlEndPoint,docType)
                .header("X-Trace-Id", MDC.get(CommonUtil.TRACE_ID))
                .contentType(MediaType.APPLICATION_JSON) // Sending JSON
                .accept(MediaType.APPLICATION_XML)       // Expecting XML
                .body(document)                 // Spring automatically serializes to JSON
                .retrieve()
                .body(String.class);                      // Captures raw XML string response
    }
}
