package com.aibizs.einvoice.einvoice_api.controller.util;

import com.aibizs.einvoice.einvoice_api.application.connector.dto.InvoiceDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.LineItemDTO;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.CreateInvoiceRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.LineItemRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.OutboundInvoiceRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CommonUtil {

    private static final AtomicLong COUNTER = new AtomicLong(0);
    private static final String INSTANCE_ID = System.getProperty("instance.id", "srv1");

    public static final String TRACE_ID = "traceId";
    public static final String QUEUE_FULL = "queue_full";
    public static final String VALIDATION_FAILED = "validation_failed";
    public static final String VALIDATED = "validated";
    public static final String PROCESSING = "processing";
    public static final String ASYNC = "async";

    public static String generateTraceId() {
        return System.currentTimeMillis() + "-" + INSTANCE_ID + "-" + COUNTER.incrementAndGet();
    }

    public static void deepCopy(CreateInvoiceRQ createInvoiceRQ, InvoiceDTO invoiceDTO) {
        BeanUtils.copyProperties(createInvoiceRQ, invoiceDTO);
        BeanUtils.copyProperties(createInvoiceRQ, invoiceDTO, "lineItems");

        List<LineItemDTO> lines = new ArrayList<>();
        if (createInvoiceRQ.getLineItems() != null) {
            for (LineItemRQ item : createInvoiceRQ.getLineItems()) {      // use your source line class name here
                LineItemDTO line = new LineItemDTO();
                BeanUtils.copyProperties(item, line);
                lines.add(line);
            }
        }
        invoiceDTO.setLineItems(lines);
    }


    public boolean wantAsync(OutboundInvoiceRequest body, String xAsync) {

        Set<String> truthyValues = Set.of("1", "true", "True", "yes", "YES");

        if (xAsync != null && truthyValues.contains(xAsync.strip())) {
            if (body.options() != null) {
                return body.options().asyncSubmit();
            }
        }
        return false;
    }

    public HttpStatus outboundHttpStatus(Map<String, Object> result, boolean asyncRequested) {
        if (result == null) {
            return HttpStatus.CREATED;
        }

        String status = Objects.toString(result.get("status"), null);

        if (QUEUE_FULL.equals(status)) {
            return HttpStatus.TOO_MANY_REQUESTS;
        }
        if (VALIDATION_FAILED.equals(status)) {
            return HttpStatus.UNPROCESSABLE_CONTENT;
        }
        if (VALIDATED.equals(status)) {
            return HttpStatus.OK;
        }

        boolean isAsync = Boolean.TRUE.equals(result.get(ASYNC));

        if (PROCESSING.equals(status) && (isAsync || asyncRequested)) {
            return HttpStatus.ACCEPTED;
        }

        return HttpStatus.CREATED;
    }
}
