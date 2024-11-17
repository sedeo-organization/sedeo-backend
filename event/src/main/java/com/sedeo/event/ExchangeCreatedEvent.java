package com.sedeo.event;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class ExchangeCreatedEvent extends ApplicationEvent {

    private final ExchangeCreatedModel exchangeCreatedModel;

    public ExchangeCreatedEvent(Object source, ExchangeCreatedModel exchangeCreatedModel) {
        super(source);
        this.exchangeCreatedModel = exchangeCreatedModel;
    }

    public ExchangeCreatedModel getExchangeCreatedModel() {
        return exchangeCreatedModel;
    }

    public record ExchangeCreatedModel(UUID exchangeId, UUID groupId, UUID debtorUserId, UUID creditorUserId, BigDecimal exchangeValue, String status) {
    }
}
