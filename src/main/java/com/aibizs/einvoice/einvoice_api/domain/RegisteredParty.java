package com.aibizs.einvoice.einvoice_api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "registered_parties")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisteredParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheme_id", length = 16, nullable = false)
    private String schemeId;

    @Column(name = "participant_id", length = 128, nullable = false)
    private String participantId;

    @Column(name = "display_name", length = 256)
    private String displayName;

    @Column(name = "trade_name", length = 256)
    private String tradeName;

    @Column(name = "vat_number", length = 64)
    private String vatNumber;

    @Column(name = "company_id", length = 64)
    private String companyId;

    @Column(name = "country_subentity_code", length = 32)
    private String countrySubentityCode;

    @Column(name = "contact_name", length = 128)
    private String contactName;

    @Column(name = "contact_telephone", length = 64)
    private String contactTelephone;

    @Column(name = "contact_email", length = 128)
    private String contactEmail;

    @Column(name = "street_name", length = 256)
    private String streetName;

    @Column(name = "city_name", length = 128)
    private String cityName;

    @Column(name = "postal_zone", length = 32)
    private String postalZone;

    @Column(name = "role", length = 32) // values: sender, receiver, sp, tax_ap, customer
    private String role;

    @Column(name = "source", length = 64) // values: smp, api, inbound, manual
    private String source;

    @Lob
    @Column(name = "onboarding_notes")
    private String onboardingNotes;

    @Column(name = "smp_status", length = 32)
    @ColumnDefault("'unknown'")
    @Builder.Default
    private String smpStatus = "unknown";

    @Column(name = "smp_checked_at")
    private LocalDateTime smpCheckedAt;

    @Column(name = "smp_check_details", columnDefinition = "json")
    private String smpCheckDetails; // Can alternatively be mapped as a Map<String, Object> using a JSON converter

    @Column(name = "is_active")
    @ColumnDefault("true")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "first_seen_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime firstSeenAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;


    // Lifecycle hooks to match datetime.utcnow and onupdate hooks
    @PrePersist
    protected void onCreate() {
        this.firstSeenAt = LocalDateTime.now();
        this.lastSeenAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastSeenAt = LocalDateTime.now();
    }

    // Equivalent to the Python @property def peppol_id
    public String getPeppolId() {
        if (this.schemeId == null || this.participantId == null) {
            return null;
        }
        return String.format("%s:%s", this.schemeId, this.participantId);
    }
}
