package com.sedeo.settlement.controllers.dto;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

public record FetchSettlementGroupSummaryResponse(List<SummaryExchange> summarisedExchanges) {
    public record SummaryExchange(UUID debtorUserId, String debtorFirstName, String debtorLastName,
                                  UUID creditorUserId, String creditorFirstName, String creditorLastName,
                                  BigDecimal summarisedExchangesValue) {
    }
}
