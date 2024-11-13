package com.sedeo.settlement.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record CreateSettlementGroupRequest(
        @NotNull UUID groupId,
        @NotBlank String title,
        @Size(min = 1) @NotEmpty Set<UUID> participantIds
) {
}
