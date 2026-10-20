package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import java.util.List;

public record InvoiceLineItemDetail(
        String name,
        String description,
        String sellersItemId,
        String buyersItemId,
        StandardItemIdentification standardItemIdentification,
        String industrialClassificationCode, // BTOM-033
        String goodsOrServices,              // BTOM-019
        String serviceTypeCode,              // BGOM-07
        CommodityClassification commodityClassification,
        String originCountry,
        List<AdditionalItemProperty> additionalItemProperties
) {
    public InvoiceLineItemDetail {
        if (industrialClassificationCode == null) industrialClassificationCode = "000000";
        if (originCountry == null) originCountry = "OM";
    }

    /**
     * Replicates the resolved_goods_or_services() helper method logic
     */
    public String resolvedGoodsOrServices() {
        String natureCode = commodityClassification != null ? commodityClassification.natureCode() : null;
        String raw = (goodsOrServices != null ? goodsOrServices : (natureCode != null ? natureCode : "G"));
        raw = raw.strip().toUpperCase();

        if (List.of("S", "SV", "SVC", "SERVICE", "SERVICES").contains(raw)) {
            return "S";
        }
        return "G";
    }
}