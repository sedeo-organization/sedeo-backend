package com.sedeo.settlement.db.model;

import java.util.UUID;

public record ParticipantEntity(UUID groupId, UUID userId, String settlementStatus) {
}
