package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.controller.dto.document.response.SubmitResponse;
import com.aibizs.einvoice.einvoice_api.domain.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DashboardStatisticsService {

    public static final String TRANSPORT_QUEUED = "queued";
    public static final String TRANSPORT_SENT = "sent";
    public static final String TRANSPORT_NOT_SENT = "not_sent";

    public static final String BUSINESS_PENDING_REVIEW = "pending_review";
    public static final String BUSINESS_NOT_APPLICABLE = "not_applicable";

    public void onOutboundValidationFailed(Document doc, String message) {

        setTransportStatus(doc, TRANSPORT_QUEUED, "VALIDATION", message);
        doc.setBusinessStatus(BUSINESS_NOT_APPLICABLE);
    }

    public void onOutboundSendFailed(Document doc, SubmitResponse record) {

        String transportMessage = "";

        //no implemantaion found
//        String transportMessage = StringUtils.hasText((String) record.get("transportError")) ? (String) record.get("transportError") : doc.getErrorMessage();
        if (StringUtils.hasLength(transportMessage)) {
            TransportFailure failure = classifyTransportFailure(transportMessage);
            setTransportStatus(doc, failure.transportStatus(), failure.errorCode(), transportMessage);
            doc.setBusinessStatus(BUSINESS_NOT_APPLICABLE);
        }
    }

    private TransportFailure classifyTransportFailure(String errorMessage) {

        if (!StringUtils.hasText(errorMessage)) {
            return new TransportFailure(TRANSPORT_QUEUED, "UNKNOWN");
        }

        if (errorMessage.contains("timeout")) {
            return new TransportFailure("retry_pending", "TIMEOUT");
        }

        if (errorMessage.contains("network")) {
            return new TransportFailure("retry_pending", "NETWORK");
        }

        return new TransportFailure("failed", "UNKNOWN");
    }

    public void onOutboundSent(Document doc) {

        setTransportStatus(doc, TRANSPORT_SENT, null, null);
        doc.setBusinessStatus(BUSINESS_PENDING_REVIEW);
        doc.setPeppolSent(Boolean.TRUE);
    }

    public void onOutboundValidatedOnly(Document doc) {

        setTransportStatus(doc, TRANSPORT_NOT_SENT, null, null);
        doc.setBusinessStatus(BUSINESS_NOT_APPLICABLE);
    }

    private void setTransportStatus(Document doc, String status, String errorCode, String message) {

        doc.setTransportStatus(status);
        doc.setTransportErrorCode(errorCode);
        doc.setTransportUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        if (StringUtils.hasText(message)) {
            doc.setErrorMessage(message);
        }
    }

    record TransportFailure(
            String transportStatus,
            String errorCode) {
    }
}
