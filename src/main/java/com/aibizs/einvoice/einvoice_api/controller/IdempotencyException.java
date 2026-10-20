package com.aibizs.einvoice.einvoice_api.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IdempotencyException extends RuntimeException {
    private final HttpStatus status;

    public IdempotencyException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
