package com.sedeo.domain.settlement.model.view;

import com.sedeo.domain.settlement.model.ExchangeStatus;
import com.sedeo.domain.settlement.model.Participant;

import java.math.BigDecimal;
import java.util.UUID;

public record ExchangeWithParticipants(UUID exchangeId, Participant debtor, Participant creditor, BigDecimal exchangeValue, ExchangeStatus status) {
}
