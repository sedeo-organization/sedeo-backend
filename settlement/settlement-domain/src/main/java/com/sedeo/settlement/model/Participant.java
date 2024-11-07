package com.sedeo.settlement.model;

import java.util.UUID;

public record Participant(UUID groupId, UUID userId, SettlementStatus status) {
}
