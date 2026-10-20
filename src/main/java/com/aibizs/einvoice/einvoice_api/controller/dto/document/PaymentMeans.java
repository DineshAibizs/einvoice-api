package com.aibizs.einvoice.einvoice_api.controller.dto.document;
import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentMeans(

        String id,

        @Schema(example = "55")
        String paymentMeansCode,

        @Schema(example = "Debit Card")
        String paymentMeansCodeName,

        String paymentId,

        CardAccount cardAccount,

        FinancialAccount payeeFinancialAccount,

        PaymentMandate paymentMandate

) {
}