package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.ExchangeRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.model.Exchange;
import com.sedeo.settlement.model.ExchangeStatus;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sedeo.settlement.model.SettlementStatus.PENDING;
import static com.sedeo.settlement.model.SettlementStatus.SETTLED;

public class ParticipantsFacade implements Participants {

    private final ParticipantRepository participantRepository;
    private final ExchangeRepository exchangeRepository;

    public ParticipantsFacade(ParticipantRepository participantRepository, ExchangeRepository exchangeRepository) {
        this.participantRepository = participantRepository;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    @Transactional
    public Either<GeneralError, Void> changeParticipantsStatus(List<UUID> participantIds, UUID groupId, SettlementStatus status) {
        //TODO: create participantRepository.update(List<Participant> participants) and add error support
        return participantRepository.findParticipantsForGroup(participantIds, groupId)
                .map(participants -> participants.stream().map(participant -> participant.withStatus(status)).toList())
                .map(participants -> participants.stream().map(participantRepository::update).toList())
                .flatMap(result -> Either.right(null));
    }

    @Override
    @Transactional
    public Either<GeneralError, Void> verifyParticipantForStatusChange(UUID participantId, UUID groupId) {
        return exchangeRepository.findExchangesInvolvingParticipant(groupId, participantId)
                .map(exchanges -> exchanges.stream().map(Exchange::status).allMatch(status -> status == ExchangeStatus.SETTLED))
                .flatMap(allExchangesAreSettled -> {
                    if (allExchangesAreSettled) {
                        return changeParticipantsStatus(List.of(participantId), groupId, SETTLED);
                    } else {
                        return changeParticipantsStatus(List.of(participantId), groupId, PENDING);
                    }
                });
    }

}
