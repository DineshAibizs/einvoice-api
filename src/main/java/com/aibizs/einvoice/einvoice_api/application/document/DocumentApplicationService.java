package com.aibizs.einvoice.einvoice_api.application.document;

import com.aibizs.einvoice.einvoice_api.application.connector.OxalisConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.PeppolConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.UblConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.ValidationConnector;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.OxalisRequestDTO;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.Result;
import com.aibizs.einvoice.einvoice_api.application.connector.dto.Violation;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.DocumentType;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.OutboundInvoiceRequest;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.response.*;
import com.aibizs.einvoice.einvoice_api.controller.util.FileSystemStoreUtil;
import com.aibizs.einvoice.einvoice_api.controller.util.XmlUtil;
import com.aibizs.einvoice.einvoice_api.domain.ProcessingEvent;
import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentApplicationService {

    private static final UUID OM_NAMESPACE = UUID.fromString("e0bc4ac8-b025-46e5-a76d-0c893fc3027e");


    private final UblConnector ublConnector;
    private final ValidationConnector validationConnector;
    private final PeppolConnector peppolConnector;
    private final OxalisConnector oxalisConnector;
    private final XmlUtil xmlUtil;
    private final DocumentService documentService;
    private final RegisteredPartyService registeredPartyService;
    private final ProcessingEventService processingEventService;
    private final DashboardStatisticsService dashBoardStaticsService;
    private final IdempotencyService idempotencyService;
    private final FileSystemStoreUtil fileSystemStoreUtil;

    public Record processInvoice(OutboundInvoiceRequest body, DocumentType documentType) {

        try {
            String idempotencyKey = body.metadata().idempotencyKey();
            switch (idempotencyService.start(idempotencyKey)) {
                case DONE ->
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Already processed: " + idempotencyKey);
                case IN_PROGRESS ->
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Still processing: " + idempotencyKey);
                case NEW -> {
                }
            }

            String txnId = UUID.randomUUID().toString();
            if (documentService.existsByDocumentNumber(body.document().invoiceNumber())) {

                Violation violation = new Violation("", "Duplicate Document number", "Document number");

                var response = new ValidationFailedResponse(txnId, "validation_failed", new Validation(false, Collections.singletonList(violation)));

                return response;
            }

            com.aibizs.einvoice.einvoice_api.domain.Document document = saveInBound(txnId, documentType.type(), body);

            String docType = documentType.type().contains("self_billing") ? "self_billing" : "billing";

            String xml = ublConnector.convertToJsonToXml(body.document(), docType);
            log.info("xml-->{}", xml);
            Document xmlDocument = xmlUtil.xmlStringToDocument(xml);
            printDoc(xmlDocument);

            String uuId = computeInvoiceUuidV5(xmlDocument);
            log.info("uuId->{}", uuId);

            injectInvoiceUuid(xmlDocument, uuId);
            printDoc(xmlDocument);

            Result validationResponse = validationConnector.validateXml(xmlDocument);
            log.info("validationResponse->{} , {}", validationResponse, validationResponse.valid());


            if (!validationResponse.valid() && body.options().validateOnly()) {

                Validation validation = new Validation(false, validationResponse.errors());
                var response = new ValidationFailedResponse(txnId, "validation_failed", validation);


                fileSystemStoreUtil.saveInFileSystem(txnId, response);
//                save The Transaction json record(response);

                saveValidationOutboundResult(txnId, "validation_failed", uuId, validation, document);
//                recordOutboundResult(txnId, response, documentId);
                idempotencyService.complete(idempotencyKey);
                return response;
            }
            if (body.options().validateOnly()) {

                Validation validation = new Validation(true, validationResponse.errors());
                var response = new ValidatedResponse(txnId, "validated", uuId, validation);

                saveValidationOutboundResult(txnId, "validated", uuId, validation, document);
//                recordOutboundResult(txnId, response, documentId);
                idempotencyService.complete(idempotencyKey);
                return response;
            }


//            _wrep_and_paths()
            String sender = body.sender().schemeId() + ":" + body.sender().participantId();
            String receiver = body.receiver().schemeId() + ":" + body.receiver().participantId();
            String wrappedXml = peppolConnector.connectPeppolAp(documentType, sender, receiver, txnId, xmlDocument);
            log.debug("wrappedXml-->{}", wrappedXml);
            String fileName = "api-" + documentType.type() + "-" + txnId + ".xml";
            String filePath = fileSystemStoreUtil.writeAsFile(fileName, wrappedXml);
////////////


//            _send_outbound // smp lookup
            boolean skipTDD = body.options().skipTdd() != null;
            OxalisRequestDTO oxalisRequestDTO = new OxalisRequestDTO(filePath, txnId, sender, receiver, skipTDD);
            boolean smpLookupOk = oxalisConnector.connectPeppolAp(oxalisRequestDTO);
            log.debug("smpLookupOk ->{}", smpLookupOk);
//            _send_tdd      // peppol submission
            ///

            String correlationId = body.metadata().correlationId();
            Peppol peppol = new Peppol(true);
            TaxReporting taxReporting = new TaxReporting(true, "test");
            Validation validation = new Validation(true, null);

            SubmitResponse submitResponse = new SubmitResponse(txnId, correlationId, "completed", documentType.type(), uuId, txnId, peppol, taxReporting, validation);

            fileSystemStoreUtil.saveInFileSystem(txnId, submitResponse);

            saveFinalOutboundResult(txnId, submitResponse, document);
            idempotencyService.complete(idempotencyKey);
            return submitResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }


    private String computeInvoiceUuidV5(Document xmlDocument) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        List<String> parts = new ArrayList<>();
        Element root = xmlDocument.getDocumentElement();

//        String schemeId = xpath.evaluate("//AccountingSupplierParty/Party/EndpointID/@schemeID", xmlDocument).trim();
        Element endpoint = child(child(child(root, CAC, "AccountingSupplierParty"), CAC, "Party"), CBC, "EndpointID");
        String schemeId = attr(endpoint, "schemeID");
        parts.add(schemeId);

//        String sellerId = xpath.evaluate("//AccountingSupplierParty/Party/EndpointID/text()", xmlDocument).trim();
        String sellerId = text(endpoint);
        parts.add(sellerId);

//        String typeCode = xpath.evaluate("//InvoiceTypeCode/text()", xmlDocument).trim();
        String typeCode = text(child(root, CBC, "InvoiceTypeCode"));
        if (typeCode.isEmpty()) {
//            typeCode = xpath.evaluate("//CreditNoteTypeCode/text()", xmlDocument).trim();
            typeCode = text(child(root, CBC, "CreditNoteTypeCode"));
        }
        parts.add(typeCode);

//        String invoiceNumber = xpath.evaluate("//ID/text()", xmlDocument).trim();
        String invoiceNumber = text(child(root, CBC, "ID"));
        parts.add(invoiceNumber);

//        String issueDate = xpath.evaluate("//IssueDate/text()", xmlDocument).trim();
        String issueDate = text(child(root, CBC, "IssueDate"));
        parts.add(issueDate);

//        String currency = xpath.evaluate("//DocumentCurrencyCode/text()", xmlDocument).trim();
        String currency = text(child(root, CBC, "DocumentCurrencyCode"));


//        String taxAmount = text(child(child(root, CAC, "TaxTotal"), CBC, "TaxAmount"));
//        String taxAmount = xpath.evaluate(String.format("//TaxTotal[TaxAmount/@currencyID='%s']/TaxAmount/text()", currency), xmlDocument).trim();

        String taxAmount = getAmount(root, "TaxTotal", "TaxAmount", currency);
        parts.add(taxAmount);

//        String totalWithTax = xpath.evaluate(String.format("//LegalMonetaryTotal/TaxInclusiveAmount[@currencyID='%s']/text()", currency), xmlDocument).trim();

        String totalWithTax = getAmount(root, "LegalMonetaryTotal", "TaxInclusiveAmount", currency);
        parts.add(totalWithTax);


        boolean hasCoreFields = !sellerId.isEmpty() && !typeCode.isEmpty() && !invoiceNumber.isEmpty() && !issueDate.isEmpty();
        if (!hasCoreFields) {
            return text(child(root, CBC, "UUID"));
//            return xpath.evaluate("//UUID/text()", xmlDocument).trim();
        }

        String complexName;
        if (taxAmount.isEmpty() || totalWithTax.isEmpty()) {
            complexName = String.join(" ", parts.subList(0, parts.size() - 2));
        } else {
            complexName = String.join(" ", parts);
        }
        log.debug("complexName-->{}", complexName);

        return Generators.nameBasedGenerator(OM_NAMESPACE).generate(complexName).toString();
    }

    private static String getAmount(Element root, String subElement, String element, String currency) {

        for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() != Node.ELEMENT_NODE || !subElement.equals(node.getLocalName()) || !CAC.equals(node.getNamespaceURI())) {
                continue;
            }

            Element amt = child((Element) node, CBC, element);

            if (currency.equals(attr(amt, "currencyID")) && !text(amt).isEmpty()) {
                return text(amt);
            }
        }
        return "";
    }

    public static final String CAC = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
    public static final String CBC = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";

    public static Element child(Element parent, String ns, String name) {

        if (parent == null) return null;

        for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE && name.equals(node.getLocalName()) && ns.equals(node.getNamespaceURI())) {
                return (Element) node;
            }
        }
        return null;
    }

    public static String text(Element element) {
        return element == null ? "" : element.getTextContent().trim();
    }

    public static String attr(Element element, String name) {
        return element == null ? "" : element.getAttribute(name).trim();
    }


    public static void injectInvoiceUuid(Document doc, String invUuid) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();

        Node uuidNode = (Node) xpath.evaluate("//*[local-name()='UUID']", doc, XPathConstants.NODE);

        if (uuidNode != null) {
            uuidNode.setTextContent(invUuid);
        } else {

            String cbcNs = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
            Element uuidEl = doc.createElementNS(cbcNs, "cbc:UUID");
            uuidEl.setTextContent(invUuid);

            Node idNode = (Node) xpath.evaluate("//*[local-name()='ID']", doc, XPathConstants.NODE);

            if (idNode != null) {
                idNode.getParentNode().insertBefore(uuidEl, idNode.getNextSibling());
            } else {
                Element root = doc.getDocumentElement();
                root.insertBefore(uuidEl, root.getFirstChild());
            }
        }
    }

    private com.aibizs.einvoice.einvoice_api.domain.Document saveInBound(String txnId, String documentType, OutboundInvoiceRequest body) {

        registeredPartyService.saveOrUpdate(body.sender().schemeId(), body.sender().participantId(), "", "", "sender", "api");
        registeredPartyService.saveOrUpdate(body.receiver().schemeId(), body.receiver().participantId(), "", "", "receiver", "api");

        String correlationId = body.metadata().correlationId();

        com.aibizs.einvoice.einvoice_api.domain.Document document = com.aibizs.einvoice.einvoice_api.domain.Document.builder()
                .transactionId(txnId)
                .correlationId(StringUtils.hasText(correlationId) ? correlationId : null)
                .direction("outbound")
                .documentType(documentType)
                .status("processing")
                .senderScheme(body.sender().schemeId())
                .senderId((body.sender().participantId()))
                .receiverScheme(body.receiver().schemeId())
                .receiverId(body.receiver().participantId())
                .documentNumber(body.document().invoiceNumber())
                .source("api")
                .build();
        document = documentService.saveDocument(document);

        ProcessingEvent processingEvent = ProcessingEvent
                .builder()
                .documentId(document.getId())
                .transactionId(txnId)
                .eventType("received")
                .status("Processing")
                .message("Outbound " + documentType + " received")
                .build();
        processingEventService.save(processingEvent);


        return document;
    }


    public void saveValidationOutboundResult(String transactionId, String status, String uuid, Validation validation, com.aibizs.einvoice.einvoice_api.domain.Document document) {

        if (document == null) return;
        try {

            document.setStatus(status);
            document.setInvoiceUuid(uuid);
            document.setValidationPassed(validation.passed());
            document.setValidationErrors(toJson(validation.errors()));

            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            if ("completed".equals(status) || "validated".equals(status) || "validation_failed".equals(status) || "send_failed".equals(status)) {
                document.setCompletedAt(now);
            }

            if ("validation_failed".equals(status) || "send_failed".equals(status)) {
                List<Violation> violations = validation.errors();
                String errorMessage = violations.isEmpty() ? null : violations.getFirst().message();
                document.setErrorMessage(errorMessage);
                document.setLastSendAt(now);
            }

            if ("validation_failed".equals(status)) {
                dashBoardStaticsService.onOutboundValidationFailed(document, document.getErrorMessage());
            } else if ("validated".equals(status)) {
                dashBoardStaticsService.onOutboundValidatedOnly(document);
            }

            ProcessingEvent event = ProcessingEvent
                    .builder()
                    .documentId(document.getId())
                    .transactionId(transactionId)
                    .eventType("completed")
                    .status(status)
                    .message(buildMessage(status, null))
                    .details(buildDetails(null, document))
                    .build();

            processingEventService.save(event);

        } catch (Exception ex) {
            log.error("recordOutboundResult failed", ex);
        }
    }

    public void saveFinalOutboundResult(String transactionId, SubmitResponse record, com.aibizs.einvoice.einvoice_api.domain.Document document) {

        if (document == null) return;
        try {

            String status = record.status();
            document.setStatus(status);
            document.setInvoiceUuid(record.invoiceUuid());
            Validation validation = record.validation();

            document.setValidationPassed(validation.passed());
            document.setValidationErrors(toJson(validation.errors()));

            Peppol peppol = record.peppol();
            document.setPeppolSent(peppol.invoiceSent());
            TaxReporting taxReporting = record.taxReporting();
            document.setTddSent(taxReporting.tddSent());


//        document.setSendAttemptCount(Integer.parseInt(record.get("sendAttempt").toString()));
//        document.setMaxSendAttempts(Integer.parseInt(record.get("maxSendAttempts").toString()));


            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            if ("completed".equals(status) || "validated".equals(status) || "validation_failed".equals(status) || "send_failed".equals(status)) {
                document.setCompletedAt(now);
            }

            if ("completed".equals(status) && peppol.invoiceSent()) {
                document.setNextRetryAt(null);
                document.setLastSendAt(now);
            }

            if ("send_failed".equals(status)) {
                document.setLastSendAt(now);
            }

            if ("validation_failed".equals(status) || "send_failed".equals(status)) {
                List<Violation> violations = validation.errors();
                String errorMessage = violations.isEmpty() ? null : violations.getFirst().message();
                document.setErrorMessage(errorMessage);
            }


            if ("validation_failed".equals(status)) {
                dashBoardStaticsService.onOutboundValidationFailed(document, document.getErrorMessage());
            } else if ("send_failed".equals(status)) {
                dashBoardStaticsService.onOutboundSendFailed(document, record);
            } else if ("completed".equals(status) && peppol.invoiceSent()) {
                dashBoardStaticsService.onOutboundSent(document);
            } else if ("validated".equals(status)) {
                dashBoardStaticsService.onOutboundValidatedOnly(document);
            }

            ProcessingEvent event = ProcessingEvent
                    .builder()
                    .documentId(document.getId())
                    .transactionId(transactionId)
                    .eventType("completed")
                    .status(status)
                    .message(buildMessage(status, record))
                    .details(buildDetails(record, document))
                    .build();

            processingEventService.save(event);

        } catch (Exception ex) {
            log.error("recordOutboundResult failed", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(Object value) {
        return value instanceof Map<?, ?> ? (Map<String, Object>) value : Collections.emptyMap();
    }

    private String buildDetails(SubmitResponse record, com.aibizs.einvoice.einvoice_api.domain.Document doc) {

        Map<String, Object> details = new HashMap<>();

        details.put("validationPassed", doc.getValidationPassed());
//        details.put("sendAttempt", record.get("sendAttempt"));
//        details.put("sendTrigger", record.get("sendTrigger"));

        return toJson(details);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return null;
        }
    }

    private String buildMessage(String status, SubmitResponse record) {
//        Object attempt = record.get("sendAttempt");
        String attempt = "0";
        return attempt == null ? "Outbound finished: " + status : "Outbound finished: " + status + " (attempt " + attempt + ")";
    }

    @Deprecated
    private void printDoc(Document xmlDocument) throws TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

        log.debug("XML:\n{}", writer);
    }
}
