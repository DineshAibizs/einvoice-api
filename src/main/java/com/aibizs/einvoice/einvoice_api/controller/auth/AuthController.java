package com.aibizs.einvoice.einvoice_api.controller.auth;

import com.aibizs.einvoice.einvoice_api.controller.dto.auth.AuthRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.auth.AuthRS;
import com.aibizs.einvoice.einvoice_api.controller.dto.auth.RefreshRQ;
import com.aibizs.einvoice.einvoice_api.controller.dto.auth.RefreshRS;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.AUTH)
public class AuthController {

    public static final String AUTH = "/api/auth/token";

    /**
     * Get JWT access token using Client ID and Client Secret. </br>
     * IMPORTANT: Set 'client_id' and 'client_secret' collection variables before running this request.</br>
     * All credentials are sent in the request body. </br>
     * The token is automatically saved to 'jwt_token' collection variable after successful authentication.</br>
     * When the access token expires, run request 1b. </br>
     * Refresh JWT Access Token with the saved refresh_token (do not call this endpoint again unless you need a new refresh token).
     *
     * @param authRQ the Auth Request containing the user credentials to be authenticated
     * @return AuthRS the AuthResponse containing the jwt token and refresh token information
     */
    @Operation(summary = "Create Authentication Token", description = "Client Authentication and generate JWT Tokens ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "JWT Authentication Tokens Generated Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthRS.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<AuthRS> createAuthenticationToken(
            @Parameter(description = "Generate Authentication Tokens", required = true) @Valid @RequestBody AuthRQ authRQ) {

        log.debug("authRQ->{}", authRQ);


        AuthRS authRS = new AuthRS();
        return ResponseEntity.ok(authRS);
    }

        /**
     * Exchange a valid refresh token for a new access token when the previous access token has expired.</br>
     * Prerequisites: run "1. Get JWT Token" first so {{refresh_token}} is set.</br>
     * <p>Request body:</br>
     * {</br>
     * "refresh": "{{refresh_token}}"</br>
     * }</br>
     * Response typically includes a new "access" token (and may return a rotated "refresh"). The test script saves them back to collection variables automatically.
     * Then continue calling APIs with:</br>
     * Authorization: Bearer {{jwt_token}}</p>
     *
     * @param refreshRQ the Refresh Request containing the user refresh token to obtain new auth token
     * @return the Refresh Response containing new jwt token and refresh token
     */
    @Operation(summary = "Refresh Authentication Token", description = "Refresh Authentication Token and Generate New JWT Tokens ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "JWT Authentication Tokens Refreshed Successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RefreshRS.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("refresh/")
    public ResponseEntity<RefreshRS> refreshToken(
            @Parameter(description = "Refresh Authentication Tokens", required = true) @Valid @RequestBody RefreshRQ refreshRQ) {

        log.debug("refreshRQ->{}", refreshRQ);


        RefreshRS refreshRS = new RefreshRS();
        return ResponseEntity.ok(refreshRS);
    }
}
