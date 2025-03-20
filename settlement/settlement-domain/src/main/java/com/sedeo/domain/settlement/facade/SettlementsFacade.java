package com.sedeo.domain.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.Settlement;
import com.sedeo.domain.settlement.model.error.SettlementGroupError;
import com.sedeo.domain.settlement.model.view.DetailedSettlement;
import com.sedeo.domain.settlement.model.view.ExchangeWithParticipants;
import com.sedeo.domain.settlement.model.view.SimpleSettlement;
import com.sedeo.event.ExchangeCreatedEvent;
import com.sedeo.event.ExchangeSettledEvent;
import com.sedeo.domain.settlement.db.ParticipantRepository;
import com.sedeo.domain.settlement.db.SettlementRepository;
import com.sedeo.domain.settlement.model.Participant;
import io.vavr.control.Either;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

public class SettlementsFacade implements Settlements {

    private final SettlementRepository settlementRepository;
    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SettlementsFacade(SettlementRepository settlementRepository, ParticipantRepository participantRepository,
                             ApplicationEventPublisher applicationEventPublisher) {
        this.settlementRepository = settlementRepository;
        this.participantRepository = participantRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Either<GeneralError, Void> createSettlement(Settlement settlement, UUID userId, UUID groupId) {
        if (settlementRepository.exists(settlement.settlementId())) {
            return Either.left(new SettlementGroupError.SettlementAlreadyExists());
        }
        Set<UUID> exchangeParticipants = settlement.aggregateSettlementParticipants(userId);

        List<Exchange> pendingExchanges = settlement.exchanges().stream().map(Exchange::withPendingStatus).toList();
        return settlement.withExchanges(pendingExchanges)
                .validateThatParticipantsBelongToGroup(participantRepository.exist(groupId, exchangeParticipants))
                .flatMap(Settlement::validateExchangeDirections)
                .flatMap(Settlement::validateTotalValue)
                .flatMap(successfulSettlement -> settlementRepository.save(successfulSettlement, groupId))
                .flatMap(savedSettlement -> this.publishExchangeCreatedEvents(savedSettlement.exchanges(), groupId))
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
            return Either.left(new SettlementGroupError.SettlementNotFound());
        }

        return participantRepository.findParticipantsForGroup(groupId)
                .flatMap(participants -> {
                    Map<UUID, Participant> participantsMap = participants.stream().distinct().collect(toMap(Participant::userId, participant -> participant));

                    boolean participantDoesNotExist = !participantsMap.containsKey(userId);
                    if (participantDoesNotExist) {
                        return Either.left(new SettlementGroupError.UserNotAuthorized());
                    }

                    return settlementRepository.findSettlement(settlementId)
                            .map(settlement -> mapSettlementToDetailedSettlement(settlement, participantsMap));
                });
    }

    @Override
    public Either<GeneralError, Void> settleExchange(UUID userId, UUID groupId, UUID settlementId, UUID exchangeId) {
        if (!participantRepository.exists(groupId, userId)) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }

        return settlementRepository.findSettlement(settlementId)
                .flatMap(settlement -> settlement.settleExchange(userId, exchangeId))
                .flatMap(settlement -> settlementRepository.update(settlement, groupId))
                .flatMap(settlement -> settlement.singleExchange(exchangeId))
                .flatMap(exchange -> publishExchangeSettledEvents(List.of(exchange), groupId))
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Either<GeneralError, List<Participant>> fetchParticipants(UUID userId, UUID groupId) {
        if (!participantRepository.exists(groupId, userId)) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }

        return participantRepository.findParticipantsForGroup(groupId);
    }

    private Either<GeneralError, Void> publishExchangeCreatedEvents(List<Exchange> exchanges, UUID groupId) {
        exchanges.forEach(exchange -> applicationEventPublisher.publishEvent(new ExchangeCreatedEvent(this, new ExchangeCreatedEvent.ExchangeCreatedModel(
                exchange.exchangeId(),
                groupId,
                exchange.debtorUserId(),
                exchange.creditorUserId(),
                exchange.exchangeValue(),
                exchange.status().name()
        ))));

        return Either.right(null);
    }

    private Either<GeneralError, Void> publishExchangeSettledEvents(List<Exchange> exchanges, UUID groupId) {
        exchanges.forEach(exchange -> applicationEventPublisher.publishEvent(new ExchangeSettledEvent(this, new ExchangeSettledEvent.ExchangeSettledModel(
                exchange.exchangeId(),
                groupId,
                exchange.debtorUserId(),
                exchange.creditorUserId(),
                exchange.exchangeValue(),
                exchange.status().name()
        ))));

        return Either.right(null);
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
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }
        return Either.right(null);
    }
}
