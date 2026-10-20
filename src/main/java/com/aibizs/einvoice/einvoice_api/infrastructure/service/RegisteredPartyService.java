package com.aibizs.einvoice.einvoice_api.infrastructure.service;

import com.aibizs.einvoice.einvoice_api.domain.RegisteredParty;
import com.aibizs.einvoice.einvoice_api.infrastructure.repository.RegisteredPartyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class RegisteredPartyService {

    private final RegisteredPartyRepository registeredPartyRepository;

    @Transactional
    public RegisteredParty saveOrUpdate(String schemeId, String participantId, String displayName, String vatNumber, String role, String source) {

        var partyOpt = registeredPartyRepository.findBySchemeIdAndParticipantId(schemeId, participantId);

        var now = LocalDateTime.now(ZoneOffset.UTC);

        RegisteredParty party = new RegisteredParty();
        if (partyOpt.isPresent()) {
             party = partyOpt.get();

            party.setLastSeenAt(now);

            if (StringUtils.hasText(displayName)) {
                party.setDisplayName(displayName);
            }

            if (StringUtils.hasText(vatNumber)) {
                party.setVatNumber(vatNumber);
            }

        }

        party.setSchemeId(schemeId);
        party.setParticipantId(participantId);
        party.setDisplayName(displayName);
        party.setVatNumber(vatNumber);
        party.setRole(role);
        party.setSource(source);
        party.setFirstSeenAt(now);
        party.setLastSeenAt(now);

        registeredPartyRepository.save(party);
        return party;
    }

    public RegisteredParty save(RegisteredParty registeredParty) {
        return registeredPartyRepository.save(registeredParty);
    }
}
