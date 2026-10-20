package com.aibizs.einvoice.einvoice_api.infrastructure.repository;

import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredPartyRepository extends JpaRepository<RegisteredParty, Long> {

    Optional<RegisteredParty> findBySchemeIdAndParticipantId(String schemeId, String participantId);
}
