package com.aibizs.einvoice.einvoice_api.infrastructure.repository;

import com.aibizs.einvoice.einvoice_api.domain.ProcessingEvent;
import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingEventRepository extends JpaRepository<ProcessingEvent, Long> {
}
