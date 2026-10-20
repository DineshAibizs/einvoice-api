package com.aibizs.einvoice.einvoice_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SenderAccessDeniedException extends RuntimeException {
    public SenderAccessDeniedException(String message) {
        super(message);
    }
}
