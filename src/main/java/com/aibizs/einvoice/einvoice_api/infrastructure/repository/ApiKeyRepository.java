package com.aibizs.einvoice.einvoice_api.infrastructure.repository;

import com.aibizs.einvoice.einvoice_api.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findByIsActiveTrueAndKeyPrefix(String prefix);
}
