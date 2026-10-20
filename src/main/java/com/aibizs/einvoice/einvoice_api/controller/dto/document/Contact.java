package com.aibizs.einvoice.einvoice_api.controller.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Contact {

    private String name;

    @Schema(example = "+968 1234 5678")
    private String telephone;

    private String email;
}
