package com.sedeo.settlement.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Settlement(UUID settlementId, String title, BigDecimal totalValue, List<Exchange> exchanges) {
}
