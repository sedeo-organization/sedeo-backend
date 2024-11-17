package com.sedeo.event;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class ExchangeSettledEvent extends ApplicationEvent {

    private final ExchangeSettledModel exchangeSettledModel;

    public ExchangeSettledEvent(Object source, ExchangeSettledModel exchangeSettledModel) {
        super(source);
        this.exchangeSettledModel = exchangeSettledModel;
    }

    public ExchangeSettledModel getExchangeSettledModel() {
        return exchangeSettledModel;
    }

    public record ExchangeSettledModel(UUID exchangeId, UUID groupId, UUID debtorUserId, UUID creditorUserId, BigDecimal exchangeValue, String status) {
    }
}
