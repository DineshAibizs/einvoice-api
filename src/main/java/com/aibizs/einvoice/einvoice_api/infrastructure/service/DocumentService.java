package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.domain.Document;
import com.aibizs.einvoice.einvoice_api.infrastructure.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document saveDocument(Document document) {

        documentRepository.save(document);
        return document;
    }

    public Optional<Document> findById(long id) {
        return documentRepository.findById(id);
    }

    public Optional<Document> findByTransactionId(String transactionId) {
        return documentRepository.findByTransactionId(transactionId);
    }

    public Optional<Document> findByDocumentNumber(String documentNumber) {
        return documentRepository.findByDocumentNumber(documentNumber);
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return documentRepository.existsByDocumentNumber(documentNumber);
    }

    public Optional<Document> findByIdOrTransactionId(long id, String transactionId){
        return documentRepository.findByIdOrTransactionId(id,transactionId);
    }
}
