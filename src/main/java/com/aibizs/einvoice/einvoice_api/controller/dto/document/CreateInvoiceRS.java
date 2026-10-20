package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Invoice/Creditnote Response Data Transfer Object")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@com.fasterxml.jackson.databind.annotation.JsonNaming(
        com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateInvoiceRS {

    private String id;
    private List<LineItemRS> lineItems;
    private List<TaxDetailRS> taxDetails;
    private String companyName;
    private CompanyDetailsRS companyDetailsRS;
    private String invoiceTypeName;
    private String customerName;
    private OmanQRRS omanQRRS;
    private String invoiceNumber;
    private String trackingNumber;
    private String trackingNumberSetAt;
    private String publicAccessToken;
    private String processedReferenceNumber;
    private String processedReferenceNumberSetAt;
    private String invoiceDate;
    private String dueDate;
    private String complianceProfile;
    private String buyerName;
    private String buyerTaxId;
    private String buyerEmail;
    private String buyerAddress;
    private String sellerName;
    private String sellerTaxId;
    private String sellerEmail;
    private String sellerAddress;
    private String invoiceTypeCode;
    private String specificationIdentifier;
    private String businessProcessType;
    private String taxMechanism;
    private boolean isCreditNote;
    private boolean isSelfBilled;

    private String vatAccountingCurrencyCode;
    private String valueAddedTaxPointDate;
    private String buyerReference;
    private String projectReference;
    private String contractReference;
    private String purchaseOrderReference;
    private String salesOrderReference;
    private String receivingAdviceReference;
    private String despatchAdviceReference;
    private String tenderOrLotReference;
    private String buyerAccountingReference;
    private String paymentTerms;
    private String invoiceNote;
    private String proceedingInvoiceNumber;
    private String proceedingInvoiceIssueDate;
    private String invoicingPeriodStartDate;
    private String invoicingPeriodEndDate;
    private String sellerTradingName;
    private String sellerIdentifier;
    private String sellerLegalRegistrationIdentifier;
    private String sellerVatIdentifier;
    private String sellerTaxRegistrationIdentifier;
    private String sellerAdditionalLegalInformation;
    private String sellerElectronicAddress;
    private String sellerAddressLine1;
    private String sellerAddressLine2;
    private String sellerCity;
    private String sellerPostalCode;
    private String sellerCountryCode;
    private String sellerCountrySubDivision;
    private String sellerContractPoint;
    private String sellerContractTelephoneNumber;
    private String sellerContractEmailAddress;
    private String buyerIdentifier;
    private String buyerTradingName;
    private String buyerLegalRegistrationIdentifier;
    private String buyerVatIdentifier;
    private String buyerElectronicAddress;
    private String buyerAddressLine1;
    private String buyerAddressLine2;
    private String buyerCity;
    private String buyerPostalCode;
    private String buyerCountryCode;
    private String buyerCountrySubDivision;
    private String invoiceUUID;
    private String invoiceTransactionType;
    private String invoiceIssueTime;
    private String invoiceObjectIdentifier;
    private String creditDebitNoteReasonCode;
    private String proceedingInvoiceUUID;
    private String paymentInvoiceNumber;
    private String paymentInvoiceUUID;
    private String sellerAddressLine3;
    private String buyerAddressLine3;
    private String sellerElectronicAddressScheme;
    private String buyerElectronicAddressScheme;
    private String sellerIdentifierScheme;
    private String buyerIdentifierScheme;
    private String sellerVatScheme;
    private String buyerVatScheme;
    private String sellerCountrySubdivisionCode;
    private String buyerCountrySubdivisionCode;
    private String buyerContactPoint;
    private String buyerContactTelephoneNumber;
    private String buyerContactEmailAddress;
    private String currencyExchangeRate;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String taxTotalAccountingCurrency;
    private String sumOfAllowances;
    private String sumOfCharges;
    private String invoiceTotalWithoutTax;
    private String paidAmount;
    private String roundingAmount;
    private String amountDueForPayment;
    private String paymentMeansCode;
    private String paymentMeansText;
    private String remittanceInformation;
    private String paymentAccountIdentifier;
    private String paymentAccountName;
    private String paymentServiceProviderIdentifier;
    private String deliverToPartyName;
    private String deliverToLocationIdentifier;
    private String actualDeliveryDate;
    private String importDate;
    private String customsDeclarationNumber;
    private String incoterms;
    private String deliverToAddressLine1;
    private String deliverToAddressLine2;
    private String deliverToAddressLine3;
    private String deliverToCity;
    private String deliverToPostalCode;
    private String deliverToCountrySubDivision;
    private String deliverToCountryCode;
    private String payeeName;
    private String payeeIdentifier;
    private String payeeLegalRegistrationIdentifier;
    private String thirdPartyName;
    private String thirdPartyVatIdentifier;
    private String documentLevelAllowanceAmount;
    private String documentLevelAllowanceReason;
    private String documentLevelAllowanceReasonCode;
    private String documentLevelChargeAmount;
    private String documentLevelChargeReason;
    private String documentLevelChargeReasonCode;

    private String subTotal;
    private String taxAmount;
    private String totalAmount;
    private String currency;
    private String status;
    private String source;
    private String documentSide;

    private String governmentReferenceCode;
    private String governmentSubmissionId;
    private String governmentSubmittedAt;
    private Map<String, Object> governmentResponse;
    private String governmentSuccessCode;

    private boolean sendToGovernment;
    private boolean sendToThirdParty;
    private String thirdPartyEndpoint;
    private String originalFilePath;
    private Map<String, Object> rowData;
    private Map<String, Object> fieldMapping;
    private Map<String, Object> metaData;
    private Map<String, Object> dynamicFields;
    private String createdAt;
    private String updatedAt;
    private String company;
    private String invoiceType;
    private String customer;
    private String createdBy;
    private QRCodeRS qrCodeRS;
    private String mode;


}
