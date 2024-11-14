package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementRepository;
import com.sedeo.settlement.model.Exchange;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.error.SettlementGroupError.SettlementNotFound;
import com.sedeo.settlement.model.error.SettlementGroupError.UserNotAuthorized;
import com.sedeo.settlement.model.error.SettlementGroupError.SettlementAlreadyExists;
import com.sedeo.settlement.model.view.DetailedSettlement;
import com.sedeo.settlement.model.view.ExchangeWithParticipants;
import com.sedeo.settlement.model.view.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

public class SettlementsFacade implements Settlements {

    private final SettlementRepository settlementRepository;
    private final ParticipantRepository participantRepository;

    public SettlementsFacade(SettlementRepository settlementRepository, ParticipantRepository participantRepository) {
        this.settlementRepository = settlementRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public Either<GeneralError, Void> createSettlement(Settlement settlement, UUID userId, UUID groupId) {
        if (settlementRepository.exists(settlement.settlementId())) {
            return Either.left(new SettlementAlreadyExists());
        }
        Set<UUID> exchangeParticipants = settlement.aggregateSettlementParticipants(userId);

        List<Exchange> pendingExchanges = settlement.exchanges().stream().map(Exchange::withPendingStatus).toList();
        return settlement.withExchanges(pendingExchanges)
                .validateThatParticipantsBelongToGroup(participantRepository.exist(groupId, exchangeParticipants))
                .flatMap(Settlement::validateExchangeDirections)
                .flatMap(Settlement::validateTotalValue)
                .flatMap(successfulSettlement -> settlementRepository.save(successfulSettlement, groupId))
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Either<GeneralError, List<SimpleSettlement>> fetchSettlements(UUID groupId, UUID userId) {
        return ifParticipantBelongsToGroup(groupId, userId)
                .flatMap(participantBelongs -> settlementRepository.find(groupId));
    }

    @Override
    public Either<GeneralError, DetailedSettlement> fetchSettlementDetails(UUID userId, UUID groupId, UUID settlementId) {
        boolean settlementDoesNotExist = !settlementRepository.exists(settlementId);
        if (settlementDoesNotExist) {
            return Either.left(new SettlementNotFound());
        }

        return participantRepository.findParticipantsForGroup(groupId)
                .flatMap(participants -> {
                    Map<UUID, Participant> participantsMap = participants.stream().collect(toMap(Participant::userId, participant -> participant));

                    boolean participantDoesNotExist = !participantsMap.containsKey(userId);
                    if (participantDoesNotExist) {
                        return Either.left(new UserNotAuthorized());
                    }

                    return settlementRepository.findSettlement(settlementId)
                            .map(settlement -> mapSettlementToDetailedSettlement(settlement, participantsMap));
                });
    }

    @Override
    public Either<GeneralError, Void> settleExchange(UUID userId, UUID groupId, UUID settlementId, UUID exchangeId) {
        if (!participantRepository.exists(groupId, userId)) {
            return Either.left(new UserNotAuthorized());
        }

        return settlementRepository.findSettlement(settlementId)
                .flatMap(settlement -> settlement.settleExchange(userId, exchangeId))
                .flatMap(settlement -> settlementRepository.update(settlement, groupId))
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Either<GeneralError, List<Participant>> fetchParticipants(UUID userId, UUID groupId) {
        if (!participantRepository.exists(groupId, userId)) {
            return Either.left(new UserNotAuthorized());
        }

        return participantRepository.findParticipantsForGroup(groupId);
    }

    private DetailedSettlement mapSettlementToDetailedSettlement(Settlement settlement, Map<UUID, Participant> participantsMap) {
        return new DetailedSettlement(
                settlement.settlementId(),
                settlement.title(),
                settlement.totalValue(),
                settlement.exchanges().stream().map(exchange -> new ExchangeWithParticipants(
                        exchange.exchangeId(),
                        participantsMap.get(exchange.debtorUserId()),
                        participantsMap.get(exchange.creditorUserId()),
                        exchange.exchangeValue(),
                        exchange.status())).toList()
        );
    }

    private Either<GeneralError, Void> ifParticipantBelongsToGroup(UUID groupId, UUID userId) {
        boolean participantDoesNotExist = !participantRepository.exists(groupId, userId);
        if (participantDoesNotExist) {
            return Either.left(new UserNotAuthorized());
        }
        return Either.right(null);
    }
}
