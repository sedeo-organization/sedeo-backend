package com.sedeo.settlement.db.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ExchangeEntity(UUID exchangeId, UUID settlementId, UUID groupId, UUID debtorUserId, UUID creditorUserId, BigDecimal exchangeValue, String status) {
}
