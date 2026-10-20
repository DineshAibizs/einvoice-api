package com.aibizs.einvoice.einvoice_api.controller;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    public final AuthErrorCode authErrorCode;

    public AuthenticationException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.authErrorCode = authErrorCode;
    }
}
