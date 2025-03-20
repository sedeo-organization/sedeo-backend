package com.sedeo.domain.settlement.model.view;

import java.math.BigDecimal;
import java.util.UUID;

public record SummaryExchange(UUID groupId, UUID debtorUserId, String debtorFirstName, String debtorLastName,
                              UUID creditorUserId, String creditorFirstName, String creditorLastName,
                              BigDecimal summarisedExchangesValue) {
}