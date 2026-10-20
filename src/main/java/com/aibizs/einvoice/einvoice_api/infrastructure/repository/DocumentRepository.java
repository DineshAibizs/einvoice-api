package com.aibizs.einvoice.einvoice_api.infrastructure.repository;

import com.aibizs.einvoice.einvoice_api.domain.Document;
import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByTransactionId(String transactionId);

    Optional<Document> findByIdOrTransactionId(long id, String transactionId);

    Optional<Document> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);

}
