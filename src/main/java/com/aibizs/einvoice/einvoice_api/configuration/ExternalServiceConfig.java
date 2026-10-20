package com.aibizs.einvoice.einvoice_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalServiceConfig {

    @Value("${einvoice.ubl.service}")
    private String ublService;

    @Value("${einvoice.validation.service}")
    private String validationService;

    @Value("${einvoice.peppol-ap.service}")
    private String peppolApService;

    @Value("${einvoice.oxalis-connector.service}")
    private String oxalisApService;

    @Bean
    public RestClient ublServiceClient() {
        return RestClient.builder()
                .baseUrl(ublService)
                .build();
    }

    @Bean
    public RestClient validationServiceClient() {
        return RestClient.builder()
                .baseUrl(validationService)
                .build();
    }

    @Bean
    public RestClient peppolApServiceClient() {
        return RestClient.builder()
                .baseUrl(peppolApService)
                .build();
    }

    @Bean
    public RestClient oxalisApServiceClient() {
        return RestClient.builder()
                .baseUrl(oxalisApService)
                .build();
    }
}
