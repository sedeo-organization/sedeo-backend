package com.sedeo.settlement.controllers.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FetchSettlementDetailsResponse(String title, BigDecimal totalValue,
                                             List<SettlementExchange> settlementExchanges) {
    public record SettlementExchange(UUID exchangeId, UUID debtorId, String debtorFirstName, String debtorLastName,
                                     UUID creditorId, String creditorFirstName, String creditorLastName, BigDecimal exchangeValue, String status) {
    }
}
