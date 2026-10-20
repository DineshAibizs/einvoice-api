package com.aibizs.einvoice.einvoice_api.controller.dto.document;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommodityClassification(

        @Schema(
                description = """
                        Legacy alias for goods/services. Prefer item.goodsOrServices (G|S).
                        Accepted: G/GS -> G, S/SV/SVC -> S.
                        Mapped to BTOM-019 DocumentTypeCode.
                        """
        )
        String natureCode,

        @Schema(
                description = "12-digit Oman HS code (listId=HS) or 8-digit service type (listId=MP)"
        )
        String itemClassificationCode,

        @Schema(
                description = "HS for goods; MP for service-type code list"
        )
        String listId

) {

    public CommodityClassification {
        if (listId == null) {
            listId = "HS";
        }
    }
}