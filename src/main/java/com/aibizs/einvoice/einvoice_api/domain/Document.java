package com.aibizs.einvoice.einvoice_api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "documents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", length = 64, unique = true)
    private String transactionId;

    @Column(name = "correlation_id", length = 128)
    private String correlationId;

    @Column(name = "direction", nullable = false, length = 16)
    private String direction;

    @Column(name = "document_type", length = 64)
    private String documentType;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "transport_status", length = 32)
    private String transportStatus;

    @Column(name = "business_status", length = 32)
    private String businessStatus;

    @Column(name = "transport_error_code", length = 64)
    private String transportErrorCode;

    @Column(name = "business_response_code", length = 64)
    private String businessResponseCode;

    @Column(name = "mls_type", length = 16)
    private String mlsType;

    @Column(name = "parent_transaction_id", length = 64)
    private String parentTransactionId;

    @Column(name = "transport_updated_at")
    private LocalDateTime transportUpdatedAt;

    @Column(name = "business_updated_at")
    private LocalDateTime businessUpdatedAt;

    @Column(name = "sender_scheme", length = 16)
    private String senderScheme;

    @Column(name = "sender_id", length = 128)
    private String senderId;

    @Column(name = "receiver_scheme", length = 16)
    private String receiverScheme;

    @Column(name = "receiver_id", length = 128)
    private String receiverId;

    @Column(name = "document_number", length = 128)
    private String documentNumber;

    @Column(name = "invoice_uuid", length = 64)
    private String invoiceUuid;

    @Column(name = "validation_passed")
    private Boolean validationPassed;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String validationErrors;

    @Column(name = "peppol_sent")
    private Boolean peppolSent;

    @Column(name = "tdd_sent")
    private Boolean tddSent;

    @Column(name = "mls_sent")
    private Boolean mlsSent;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_summary", columnDefinition = "jsonb")
    private String payloadSummary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "document_json", columnDefinition = "jsonb")
    private String documentJson;

    @Column(name = "source", length = 32)
    private String source;

    @Column(name = "send_attempt_count")
    private Integer sendAttemptCount;

    @Column(name = "max_send_attempts")
    private Integer maxSendAttempts;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    @Column(name = "last_send_at")
    private LocalDateTime lastSendAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "outbound_artifacts", columnDefinition = "jsonb")
    private String outboundArtifacts;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (sendAttemptCount == null) {
            sendAttemptCount = 0;
        }

        if (maxSendAttempts == null) {
            maxSendAttempts = 3;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
