package com.sedeo.settlement.model.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DetailedSettlement(UUID settlementId, String title, BigDecimal totalValue, List<ExchangeWithParticipants> exchanges) {
}
