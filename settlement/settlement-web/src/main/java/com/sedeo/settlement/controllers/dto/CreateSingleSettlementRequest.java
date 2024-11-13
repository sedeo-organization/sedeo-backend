package com.sedeo.settlement.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateSingleSettlementRequest(
        @NotNull UUID settlementId,
        @NotBlank String title,
        @NotNull  BigDecimal totalValue,
        @Size(min = 1) @NotEmpty @Valid List<SettlementExchange> settlementExchanges
) {
    public record SettlementExchange(
            @NotNull UUID exchangeId,
            @NotNull UUID debtorUserId,
            @NotNull UUID creditorUserId,
            @NotNull BigDecimal value
    ) {
    }
}
