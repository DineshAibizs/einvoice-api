package com.aibizs.einvoice.einvoice_api.controller.document;

import com.aibizs.einvoice.einvoice_api.application.document.DocumentApplicationService;
import com.aibizs.einvoice.einvoice_api.controller.dto.auth.ApiKeyContext;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.DocumentType;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.OutboundInvoiceRequest;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.response.SubmitResponse;
import com.aibizs.einvoice.einvoice_api.controller.util.CommonUtil;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(DocumentController.ROOT)
public class DocumentController {

    public static final String ROOT = "/api/v1/documents";


    private final ApiKeyService apiKeyService;
    private final CommonUtil commonUtil;
    private final DocumentApplicationService documentApplicationService;

    @PostMapping("/outbound/invoice")
    public ResponseEntity<?> postOutboundInvoice(
            @RequestBody OutboundInvoiceRequest body,
            @RequestHeader(value = "X-Async", required = false) String xAsync,
            Authentication authentication, HttpServletRequest request) {

        ApiKeyContext ctx = (ApiKeyContext) authentication.getPrincipal();

        apiKeyService.enforceSender(ctx, body.sender().schemeId(), body.sender().participantId());

//        boolean asyncRequested = commonUtil.wantAsync(body, xAsync);

//        Record response = documentApplicationService.processInvoice(body, "pint_om_invoice");
        Record response = documentApplicationService.processInvoice(body, DocumentType.PINT_OM_INVOICE);
        log.debug("response->{}", response);


        // call service business logic
//        Map<String, Object> result = outboundService.submitInvoice(body, "pint_om_invoice");
//        Map<String, Object> result = null;
//
//
//        String status = (String) result.get("status");
//
//        if ("queue_full" .equals(status)) {
//            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, result.toString());
//        }
//
//        if ("validation_failed" .equals(status)) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, result.toString());
//        }

//        HttpStatus responseStatus = commonUtil.outboundHttpStatus(result, asyncRequested);
//        return ResponseEntity.status(responseStatus).body(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
