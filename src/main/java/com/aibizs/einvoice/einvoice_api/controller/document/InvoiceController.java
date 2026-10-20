package com.aibizs.einvoice.einvoice_api.controller.document;

import com.aibizs.einvoice.einvoice_api.application.document.InvoiceApplicationService;
import com.aibizs.einvoice.einvoice_api.controller.dto.document.*;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.IdempotencyService;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "Invoice", description = "Invoice/Creditnote management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping(InvoiceController.INVOICE)
public class InvoiceController {

    public static final String INVOICE = "/api/invoices";

    private final IdempotencyService idempotencyService;
    private final InvoiceService invoiceService;
    private final InvoiceApplicationService invoiceApplicationService;


    /**
     * <p>Oman OTA Full Tax (B2B) standard invoice. BTOM-001=1XXXXXXXXXXXXXXXXXXX. For B2C Simplified use request 2c with X1XXXXXXXXXXXXXXXXXX. Same BT-3 code 380. Success response includes qr_code.image_data_url and qr_code.tlv_base64 for printing the OTA QR on the invoice.</p>
     * <p>Buyer identity: send customer_erp_uuid (ERP UUID) and/or customer_code (Customer Code e.g. ASCH001) from /customer/customers/. At least one is required.</p>
     * <p>Use invoice_type STD UUID for BT-3 380. Do not use OM0001. OMR amounts: 3dp.</p>
     * <p>Unit of measure (BT-130 / BR-23): every line_items[] entry must include unit_of_measure as a UN/ECE Recommendation 20 code (e.g. H87, E48, KGM). Mandatory for Full Tax and Simplified.</p>
     * <p>Industrial classification (BTOM-033 ISIC): every Full Tax line_items[] entry should include industrial_classification_code as a 6-digit Oman ISIC (schemeName=CC), except Simplified / import / profit-margin-self (IBR-081-OM). Code list: https://docs.peppol.eu/poac/om/pint-om/trn-creditnote/codelist/ISIC/</p>
     *
     * @param createInvoiceRQ the invoice or credit note request to be submitted to peppol network
     * @return CreateInvoiceRS the response of submitted invoice
     */
    @Operation(summary = "Create Invoice/CreditNote", description = "Create Invoice or Credit note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Invoice/Credit Note Submitted Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateInvoiceRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<CreateInvoiceRS> createInvoice(
            @Parameter(description = "Invoice or CreditNote to be Submitted", required = true) @Valid @RequestBody CreateInvoiceRQ createInvoiceRQ) {

        log.debug("createRequest->{}", createInvoiceRQ);
        String key = createInvoiceRQ.getInvoiceNumber();
        switch (idempotencyService.start(key)) {
            case DONE -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Already processed: " + key);
            case IN_PROGRESS -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Still processing: " + key);
            case NEW -> {
            }
        }

        CreateInvoiceRS createInvoiceRS = invoiceApplicationService.processInvoice(createInvoiceRQ);

        idempotencyService.complete(key);
        return ResponseEntity.ok(createInvoiceRS);
    }


    /**
     * <p>SINGLE ENTRY approach — POST /api/invoices/single-entry/create/
     * Buyer party is sent on the document header (Peppol scheme, Participant ID, VAT, CR, legal/trade name, governorate, address, contact). No Customer Master sync required.
     * Existing POST /api/invoices/create/ (Customer Master) is unchanged.</p>
     * <p>Oman OTA Full Tax (B2B) standard invoice. BTOM-001=1XXXXXXXXXXXXXXXXXXX. For B2C Simplified use request 2c with X1XXXXXXXXXXXXXXXXXX. Same BT-3 code 380. Success response includes qr_code.image_data_url and qr_code.tlv_base64 for printing the OTA QR on the invoice.</p>
     * <p>Buyer identity: send buyer_* Peppol/OTA fields on the document header. Optional customer_code / customer_erp_uuid are caller refs only (not resolved against Customer Master).</p>
     * <p>Use invoice_type STD UUID for BT-3 380. Do not use OM0001. OMR amounts: 3dp.</p>
     * <p>Unit of measure (BT-130 / BR-23): every line_items[] entry must include unit_of_measure as a UN/ECE Recommendation 20 code (e.g. H87, E48, KGM). Mandatory for Full Tax and Simplified.</p>
     * <p>Industrial classification (BTOM-033 ISIC): every Full Tax line_items[] entry should include industrial_classification_code as a 6-digit Oman ISIC (schemeName=CC), except Simplified / import / profit-margin-self (IBR-081-OM). Code list: https://docs.peppol.eu/poac/om/pint-om/trn-creditnote/codelist/ISIC/</p>
     *
     * @param invoiceSingleEntryRQ the invoice single entry to be submitted to peppol network
     * @return CreateInvoiceRS the response of submitted invoice
     */
    @Operation(summary = "Create Invoice/CreditNote SingleEntry", description = "Create Invoice or Credit note SingleEntry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Invoice/Credit Note SingleEntry Submitted Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateInvoiceRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/single-entry/create/")
    public ResponseEntity<CreateInvoiceRS> createSingleEntry(
            @Parameter(description = "Invoice or CreditNote SingleEntry to be Submitted", required = true) @Valid @RequestBody InvoiceSingleEntryRQ invoiceSingleEntryRQ) {

        log.debug("createSingleEntry->{}", invoiceSingleEntryRQ);
        String key = invoiceSingleEntryRQ.getInvoiceNumber();

        switch (idempotencyService.start(key)) {
            case DONE -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Already processed: " + key);
            case IN_PROGRESS -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Still processing: " + key);
            case NEW -> {
            }
        }

        CreateInvoiceRS createInvoiceRS = new CreateInvoiceRS();


        idempotencyService.complete(key);
        return ResponseEntity.ok(createInvoiceRS);
    }


    /**
     * <p>Get Tracking Number, Processed Reference (falls back to OTA government_success_code), government_success_code, final qr_code (image_data_url / scan_url / tlv_base64 for ERP), and timestamps by invoice_number.</p>
     *
     * @param invoiceNumber the invoice number to be searched by
     * @return InvoiceReferencesRS the response invoice references object
     */
    @Operation(summary = "Get Invoice References", description = "Get Invoice References")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice References",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvoiceReferencesRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/by-number/{invoiceNumber}/references/")
    public ResponseEntity<InvoiceReferencesRS> getInvoiceReferences(@PathVariable String invoiceNumber) {

        log.debug("getInvoiceReferences->{}", invoiceNumber);
        InvoiceReferencesRS invoiceReferencesRS = new InvoiceReferencesRS();
        return ResponseEntity.ok(invoiceReferencesRS);
    }


    /**
     * <p>Returns recorded OTA/PEPPOL compliance assessments. Pass Invoice # in the path (e.g. INV-OM-B2C-2026-001). UUID also works.</p>
     *
     * @param invoiceNumber the invoice number to be searched by
     * @return ComplianceEvidencesRS the compliance evidences response
     */
    @Operation(summary = "Get Invoice Compliance Evidence", description = "Get Invoice Compliance Evidence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice Compliance Evidences",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ComplianceEvidencesRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{invoiceNumber}/compliance/")
    public ResponseEntity<List<ComplianceEvidencesRS>> getInvoiceComplianceEvidence(@PathVariable String invoiceNumber) {

        log.debug("getInvoiceComplianceEvidence->{}", invoiceNumber);
        List<ComplianceEvidencesRS> complianceEvidencesRSList = new ArrayList<>();
        return ResponseEntity.ok(complianceEvidencesRSList);
    }


    /**
     * <p>Returns generated Oman TDD payload/status. Pass Invoice # in the path (e.g. INV-OM-B2C-2026-001). UUID also works</p>
     *
     * @param invoiceNumber the invoice number to be searched by
     * @return InvoiceTDDReportRS the invoice tdd report
     */
    @Operation(summary = "Get Invoice TDD Report", description = "Get Invoice TDD Report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice TDD Report",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvoiceTDDReportRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{invoiceNumber}/tdd-report/")
    public ResponseEntity<InvoiceTDDReportRS> getInvoiceTddReport(@PathVariable String invoiceNumber) {

        log.debug("getInvoiceTddReport->{}", invoiceNumber);
        InvoiceTDDReportRS invoiceTDDReportRS = new InvoiceTDDReportRS();
        return ResponseEntity.ok(invoiceTDDReportRS);
    }


    /**
     * <p>OTA converter dry-run JSON (validateOnly — not submitted). Shows document.transactionType in 0/1 form (X from create mapped to 0), amounts at Document Rule precision (3dp OMR), and buyer vatNumber omission for Simplified + party_kind=consumer. Also: /api/invoices/by-number//ota-preview/</p>
     *
     * @param invoiceNo the invoice number to be searched by
     * @return InvoiceOTAConverterPreviewRS the invoice OTA converter preview
     */
    @Operation(summary = "Get Invoice OTA Converter Preview", description = "Get Invoice OTA Converter Preview")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice Summary by Tracking Number",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvoiceOTAConverterPreviewRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{invoiceNo}/ota-preview/")
    public ResponseEntity<InvoiceOTAConverterPreviewRS> getInvoiceOTAConverterPreview(@PathVariable String invoiceNo) {

        log.debug("getInvoiceOTAConverterPreview->{}", invoiceNo);
        InvoiceOTAConverterPreviewRS invoiceOTAConverterPreviewRS = new InvoiceOTAConverterPreviewRS();
        return ResponseEntity.ok(invoiceOTAConverterPreviewRS);
    }


    /**
     * <p>Returns one invoice summary (same fields as Excel export Summary tab) for the given tracking_number. Set collection variable tracking_number to the exact tracking value.</p>
     *
     * @param trackingNumber the tracking number to be search by
     * @return InvoiceSummaryRS the invoice summary response
     */
    @Operation(summary = "Get Invoice Summary by Tracking Number", description = "Get Invoice Summary by Tracking Number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice Summary by Tracking Number",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvoiceSummaryRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/summary/by-tracking/{trackingNumber}/")
    public ResponseEntity<InvoiceSummaryRS> getInvoiceSummaryByTrackingNo(@PathVariable String trackingNumber) {

        log.debug("getInvoiceSummaryByTrackingNo->{}", trackingNumber);
        InvoiceSummaryRS invoiceSummaryRS = new InvoiceSummaryRS();
        return ResponseEntity.ok(invoiceSummaryRS);
    }


    /**
     * <p>Returns all invoice summaries in the date range (max 500). Response includes count, date_from, date_to, document_side, and results array with Summary-tab fields.</p>
     *
     * @param dateFrom          the date from parameter to be searched by - Required. Invoice issue date from (YYYY-MM-DD).
     * @param dateTo            the date to parameter to be searched by - Required. Invoice issue date to (YYYY-MM-DD).
     * @param status            the status parameter to be searched by - Optional. Filter by status (draft, submitted, processed, etc.).
     * @param complianceProfile the compliance profile to be searched by - Optional. Filter by compliance_profile (standard, credit_note, etc.).
     * @return InvoiceSummariesRS the invoice summaries
     */
    @Operation(summary = "List Invoice Summaries by criteria", description = "List Invoice Summaries by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice Summaries by criteria",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvoiceSummariesRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/summary/")
    public ResponseEntity<InvoiceSummariesRS> getInvoiceSummariesByCriteria(
            @Parameter(description = "Required. Invoice issue date from (YYYY-MM-DD).", required = true) @RequestParam(name = "date_from") String dateFrom,
            @Parameter(description = "Required. Invoice issue date to (YYYY-MM-DD).", required = true) @RequestParam(name = "date_to") String dateTo,
            @Parameter(description = "Optional. Filter by status (draft, submitted, processed, etc.).", required = false) @RequestParam(name = "status") String status,
            @Parameter(description = "Optional. Filter by compliance_profile (standard, credit_note, etc.).", required = false) @RequestParam(name = "compliance_profile") String complianceProfile
    ) {

        log.debug("getInvoiceSummaryByTrackingNo- dateFrom->{}, dateTo->{}, status->{}, complianceProfile->{}", dateFrom, dateTo, status, complianceProfile);
        InvoiceSummariesRS invoiceSummariesRS = new InvoiceSummariesRS();
        return ResponseEntity.ok(invoiceSummariesRS);
    }


    /**
     * <p>Update an existing invoice. Replace {{invoice_id}} with the UUID of the invoice you want to update. Use the same field structure as create request. Include vat_accounting_currency_code (BT-6) and, when invoice currency differs from VAT currency, currency_exchange_rate (BTOM-003), source_currency_code (BTOM-028), and target_currency_code (BTOM-029).</p>
     * <p>Unit of measure (BT-130 / BR-23): every line_items[] entry must include unit_of_measure as a UN/ECE Recommendation 20 code (e.g. H87, E48, KGM). Mandatory for Full Tax and Simplified.</p>
     * <p>Industrial classification (BTOM-033 ISIC): every Full Tax line_items[] entry should include industrial_classification_code as a 6-digit Oman ISIC (schemeName=CC), except Simplified / import / profit-margin-self (IBR-081-OM). Code list: https://docs.peppol.eu/poac/om/pint-om/trn-creditnote/codelist/ISIC/</p>
     *
     * @param invoiceId       the invoice id  to be updated
     * @param updateInvoiceRQ the invoice object properties to be updated
     * @return UpdateInvoiceRS the update invoice response
     */
    @Operation(summary = "Update Invoice/CreditNote", description = "Update Invoice or Credit note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice/Credit Note Updated Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateInvoiceRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/{invoiceId}")
    public ResponseEntity<UpdateInvoiceRS> updateInvoice(
            @PathVariable String invoiceId,
            @Parameter(description = "Invoice or CreditNote to be Updated", required = true) @Valid @RequestBody UpdateInvoiceRQ updateInvoiceRQ) {

        log.debug("invoiceId->{}, updateInvoiceRQ->{}", invoiceId, updateInvoiceRQ);
        UpdateInvoiceRS updateInvoiceRS = new UpdateInvoiceRS();
        return ResponseEntity.ok(updateInvoiceRS);
    }

    /**
     * <p>Delete an invoice. Replace {{invoice_id}} with the UUID of the invoice you want to delete.</p>
     *
     * @param invoiceId the invoice id to be deleted
     * @return Void
     */
    @Operation(summary = "Delete Invoice/CreditNote", description = "Delete Invoice or Credit note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Invoice/Credit Note Deleted Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateInvoiceRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {

        log.debug("invoiceId->{}", invoiceId);

        return ResponseEntity.noContent().build();
    }

}
