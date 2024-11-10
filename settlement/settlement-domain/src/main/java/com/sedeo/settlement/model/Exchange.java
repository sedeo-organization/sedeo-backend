package com.sedeo.settlement.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Exchange(UUID exchangeId, UUID debtorUserId, UUID creditorUserId, BigDecimal value) {
}
