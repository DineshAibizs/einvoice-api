package com.aibizs.einvoice.einvoice_api.controller.customer;

import com.aibizs.einvoice.einvoice_api.controller.dto.customer.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Customer", description = "Customer / Business Party management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping(CustomerController.CUSTOMERS)
public class CustomerController {

    public static final String CUSTOMERS = "/api/invoices/customers/erp";

    /**
     * <b>B2B business party — same required fields as Add Party (Party kind = Business).</b></br></br>
     * tax_id / registration_number / postal_code / PEPPOL endpoint REQUIRED.</br>
     * Oman tax_id examples: OM1234567890 or OM110048187X.</br>
     * <p>Response: action (created|updated) + customer object. Use customer.erp_uuid as customer_erp_uuid on invoice create.
     * </p></br>
     * <b>B2C shared walk-in consumer — same as Add Party (Party kind = Consumer).</b></br></br>
     * tax_id, registration_number, and PEPPOL endpoint must be blank/omitted.</br>
     * Reuse erp_uuid / customer_code on every Simplified folio. Do not create one party per guest.
     *
     * @param createCustomerRQ the create customer request contains the information for create customer or business party
     * @return CreateCustomerRS the response object of created customer entity
     */
    @Operation(summary = "Create B2B/B2C Customer", description = "Create B2B or B2C Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Customer created Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateCustomerRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/sync/")
    public ResponseEntity<CreateCustomerRS> createCustomer(
            @Parameter(description = "B2B or B2C Customer to be Created", required = true) @Valid @RequestBody CreateCustomerRQ createCustomerRQ) {

        log.debug("createCustomerRQ->{}", createCustomerRQ);
        CreateCustomerRS createCustomerRS = new CreateCustomerRS();
        return ResponseEntity.ok(createCustomerRS);
    }

    /**
     * <p>List Customer Master parties for your company (business and consumer).</p>
     *
     * @param isActive   boolean the status of the customer entry - Optional: true|false — filter active parties
     * @param hasErpUuid boolean the ERP uuid availability - Optional: true = only parties linked to an ERP UUID
     * @param limit      int the size of return list - Optional: max rows (1–500, default 200)
     * @return ListCustomersRS the list of List customer RS entries
     */
    @Operation(summary = "List Customers B2B and B2C", description = "List All the Customers B2B and B2C")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List All the Customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListCustomersRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<ListCustomersRS> listCustomers(
            @Parameter(description = "Optional: true|false — filter active parties", required = true) @RequestParam(name = "is_active", defaultValue = "false") boolean isActive,
            @Parameter(description = "Optional: true = only parties linked to an ERP UUID", required = true) @RequestParam(name = "has_erp_uuid", defaultValue = "false") boolean hasErpUuid,
            @Parameter(description = "Optional: max rows (1–500, default 200)", required = true) @RequestParam(name = "limit", defaultValue = "200") int limit
    ) {

        log.debug("isActive->{} ,hasErpUuid->{} ,limit->{}", isActive, hasErpUuid, limit);
        ListCustomersRS listCustomersRS = new ListCustomersRS();
        return ResponseEntity.ok(listCustomersRS);
    }

    /**
     * <p>Retrieve one party using your ERP UUID (works for business or consumer).</p>
     *
     * @param erpUuid the ERP UUID of the customer entry
     * @return ResultRS the customer entry
     */
    @Operation(summary = "Get Customers by ERP UUID", description = "Get the Customers by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Get the Customer by UUID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResultRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/by-erp-uuid/{erpUuid}/")
    public ResponseEntity<ResultRS> getCustomer(@PathVariable String erpUuid) {

        log.debug("erpUuid->{}", erpUuid);
        ResultRS resultRS = new ResultRS();
        return ResponseEntity.ok(resultRS);
    }

    /**
     * <p>Partial update of an existing party identified by erp_uuid. Comments in body explain party_kind switches.</p>
     *
     * @param erpUuid          the ERP UUID of the customer entry to be updated
     * @param updateCustomerRQ the object with updating properties
     * @return UpdateCustomerRS the updated customer entry
     */
    @Operation(summary = "Update Customer", description = "Update the Customers B2B and B2C")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Update the Customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCustomerRS.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PatchMapping("/by-erp-uuid/{erpUuid}/")
    public ResponseEntity<UpdateCustomerRS> updateCustomer(
            @PathVariable String erpUuid,
            @Parameter(description = "B2B or B2C Customer to be Updated", required = true) @Valid @RequestBody UpdateCustomerRQ updateCustomerRQ) {

        log.debug("erpUuid->{} , updateCustomerRQ ->{}", erpUuid, updateCustomerRQ);
        UpdateCustomerRS updateCustomerRS = new UpdateCustomerRS();
        return ResponseEntity.ok(updateCustomerRS);
    }
}
