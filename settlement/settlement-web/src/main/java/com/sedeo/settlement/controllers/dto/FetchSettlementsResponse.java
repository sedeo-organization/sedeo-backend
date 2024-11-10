package com.sedeo.settlement.controllers.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FetchSettlementsResponse(List<Settlement> settlements) {
    public record Settlement(UUID settlementId, String title, BigDecimal totalValue) { }
}
