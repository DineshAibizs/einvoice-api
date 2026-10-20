package com.aibizs.einvoice.einvoice_api.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AuthErrorCode {
    TOKEN_EXPIRED("AUTH_001", "Access Token Has Expired"),
    TOKEN_INVALID_SIGNATURE("AUTH_002", "Token Signature Invalid"),
    TOKEN_MALFORMED("AUTH_003", "Token is malformed"),
    TOKEN_UNSUPPORTED("AUTH_004", "Token Type is Unsupported"),
    TOKEN_CLAIMS_EMPTY("AUTH_005", "Token claims are empty");

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
