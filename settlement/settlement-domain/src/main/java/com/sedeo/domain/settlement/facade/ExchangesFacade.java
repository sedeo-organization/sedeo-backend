package com.sedeo.domain.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.error.SettlementGroupError;
import com.sedeo.event.ExchangeSettledEvent;
import com.sedeo.domain.settlement.db.ExchangeRepository;
import com.sedeo.domain.settlement.db.ParticipantRepository;
import io.vavr.control.Either;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static com.sedeo.domain.settlement.model.ExchangeStatus.PENDING;

public class ExchangesFacade implements Exchanges {

    private final ExchangeRepository exchangeRepository;
    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ExchangesFacade(ExchangeRepository exchangeRepository, ParticipantRepository participantRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.exchangeRepository = exchangeRepository;
        this.participantRepository = participantRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Either<GeneralError, Void> bulkSettleExchangesForParticipantsWithinGroup(UUID groupId, UUID debtorUserId, UUID creditorUserId, UUID userId) {
        if (!participantRepository.exists(groupId, userId) || !userId.equals(creditorUserId)) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }

        return exchangeRepository.findExchangesInvolvingParticipantsWithinGroupByStatuses(groupId, debtorUserId, creditorUserId, List.of(PENDING))
                .map(exchanges -> exchanges.stream().map(Exchange::withSettledStatus).toList())
                .flatMap(exchangeRepository::updateExchangesStatus)
                .flatMap(exchanges -> publishExchangeSettledEvents(exchanges, groupId))
                .flatMap(result -> Either.right(null));
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
}
