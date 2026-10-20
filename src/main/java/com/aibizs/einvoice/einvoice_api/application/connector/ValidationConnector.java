package com.aibizs.einvoice.einvoice_api.application.connector;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.JsonToUblDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.Result;
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
public class ValidationConnector {

    @Value("${einvoice.validation.service.validate.xml}")
    private String getValidationEndPoint;

    private final RestClient validationServiceClient;
    private final XmlUtil xmlUtil;

    public Result validateXml(Document document) {

        byte[] xmlBytes = xmlUtil.documentToBytes(document);
        return validationServiceClient.post()
                .uri(getValidationEndPoint)
                .header("X-Trace-Id", MDC.get(CommonUtil.TRACE_ID))
                .contentType(MediaType.APPLICATION_XML) // Sending JSON
                .accept(MediaType.APPLICATION_JSON)       // Expecting XML
                .body(xmlBytes)                 // Spring automatically serializes to JSON
                .retrieve()
                .body(Result.class);                      // Captures raw XML string response
    }
}
