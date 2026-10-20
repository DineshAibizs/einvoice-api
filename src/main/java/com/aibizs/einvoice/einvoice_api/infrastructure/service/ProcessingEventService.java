package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.domain.ProcessingEvent;
import com.aibizs.einvoice.einvoice_api.infrastructure.repository.ProcessingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessingEventService {

    private final ProcessingEventRepository processingEventRepository;

    public ProcessingEvent save(ProcessingEvent processingEvent) {
        return processingEventRepository.save(processingEvent);
    }
}
