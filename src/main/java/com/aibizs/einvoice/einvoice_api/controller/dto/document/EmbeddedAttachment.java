package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmbeddedAttachment(

        String mimeCode,

        String filename,

        @Schema(description = "Base64-encoded file content")
        String content

) {

    public EmbeddedAttachment {
        if (mimeCode == null) {
            mimeCode = "application/pdf";
        }
    }
}