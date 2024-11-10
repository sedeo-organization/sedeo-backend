package com.sedeo.settlement.controllers.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateSingleSettlementRequest(UUID settlementId, String title, BigDecimal totalValue, List<SettlementExchange> settlementExchanges) {
    public record SettlementExchange(UUID exchangeId, UUID debtorUserId, UUID creditorUserId, BigDecimal value) { }
}
