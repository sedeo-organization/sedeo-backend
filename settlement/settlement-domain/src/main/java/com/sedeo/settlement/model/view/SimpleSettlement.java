package com.sedeo.settlement.model.view;

import java.math.BigDecimal;
import java.util.UUID;

public record SimpleSettlement(UUID settlementId, String title, BigDecimal totalValue) {
}
