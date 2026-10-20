package com.aibizs.einvoice.einvoice_api.configuration;

import com.aibizs.einvoice.einvoice_api.controller.dto.auth.ApiKeyContext;
import com.aibizs.einvoice.einvoice_api.infrastructure.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-Key");
        ApiKeyContext ctx = apiKeyService.verifyKey(apiKey);

        if (ctx == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing X-API-Key");
            return;
        }

        request.setAttribute("apiKeyContext", ctx);
        Authentication authentication = new UsernamePasswordAuthenticationToken(ctx, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
