package com.sedeo.settlement.repository.db.model;

import java.math.BigDecimal;
import java.util.UUID;

public record SettlementEntity(UUID settlementId, UUID groupId, String title, BigDecimal totalValue) {
}
