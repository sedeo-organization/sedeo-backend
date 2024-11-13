package com.sedeo.settlement.model.view;

import com.sedeo.settlement.model.Participant;

import java.math.BigDecimal;
import java.util.UUID;

public record ExchangeWithParticipants(UUID exchangeId, Participant debtor, Participant creditor, BigDecimal value) {
}
