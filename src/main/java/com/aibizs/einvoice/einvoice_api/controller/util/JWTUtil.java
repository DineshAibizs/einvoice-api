package com.aibizs.einvoice.einvoice_api.controller.util;

import com.aibizs.einvoice.einvoice_api.controller.AuthErrorCode;
import com.aibizs.einvoice.einvoice_api.controller.AuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {

    public static final String CLIENT_ID = "client_id";

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String clientId, String userId, String apiKeyId) {
        return Jwts.builder()
                .claim("token_type", "access")  // token_type
                .claim("user_id", userId)             // user_id
                .claim("api_key_id", apiKeyId)        // api_key_id
                .claim(CLIENT_ID, clientId)                 // client_id
                .id(UUID.randomUUID().toString())           // jti
                .issuedAt(new Date())                       // iat
                .expiration(new Date(System.currentTimeMillis() + expiration)) // exp
                .signWith(getSigningKey())
                .compact();

    }

    public String extractClientId(String token) {
        return extractClaims(token).get(CLIENT_ID).toString();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String clientId = extractClientId(token);
        return clientId.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public void validateToken(String token) {
        extractClaims(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (SignatureException e) {
            throw new AuthenticationException(AuthErrorCode.TOKEN_INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            throw new AuthenticationException(AuthErrorCode.TOKEN_MALFORMED);
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationException(AuthErrorCode.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException(AuthErrorCode.TOKEN_CLAIMS_EMPTY);
        }
    }
}
