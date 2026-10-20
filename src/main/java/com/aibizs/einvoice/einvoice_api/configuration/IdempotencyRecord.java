package com.aibizs.einvoice.einvoice_api.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRecord {
    private String status;

    private String requestHash;

    private Object response;

    private Instant createdAt;
}
