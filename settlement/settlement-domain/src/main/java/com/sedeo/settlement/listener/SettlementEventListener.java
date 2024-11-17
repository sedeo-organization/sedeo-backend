package com.sedeo.settlement.listener;

import com.sedeo.event.ExchangeCreatedEvent;
import com.sedeo.event.ExchangeSettledEvent;
import com.sedeo.settlement.facade.Participants;
import com.sedeo.settlement.model.SettlementStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class SettlementEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementEventListener.class);

    private final Participants participants;

    public SettlementEventListener(Participants participants) {
        this.participants = participants;
    }

    @EventListener
    public void handleExchangeCreatedEvent(ExchangeCreatedEvent exchangeCreatedEvent) {
        ExchangeCreatedEvent.ExchangeCreatedModel exchangeCreatedModel = exchangeCreatedEvent.getExchangeCreatedModel();
        List<UUID> userIds = List.of(exchangeCreatedModel.creditorUserId(), exchangeCreatedModel.debtorUserId());
        participants.changeParticipantsStatus(userIds, exchangeCreatedModel.groupId(), SettlementStatus.PENDING)
                .peekLeft(error -> LOGGER.warn("Status change failed"))
                .peek(user -> LOGGER.info("Status changed successfully"));
    }

    @EventListener
    public void handleExchangeSettledEvent(ExchangeSettledEvent exchangeSettledEvent) {
        ExchangeSettledEvent.ExchangeSettledModel exchangeSettledModel = exchangeSettledEvent.getExchangeSettledModel();
        Stream.of(exchangeSettledModel.creditorUserId(), exchangeSettledModel.debtorUserId())
                .map(participant -> participants.verifyParticipantForStatusChange(participant, exchangeSettledModel.groupId()))
                .forEach(result -> result.peekLeft(error -> LOGGER.warn("Users exchange verification failed"))
                        .peek(user -> LOGGER.info("Users exchange verification successful"))
                );

    }
}
