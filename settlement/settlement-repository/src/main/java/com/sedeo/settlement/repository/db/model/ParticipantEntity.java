package com.sedeo.settlement.repository.db.model;

import java.util.UUID;

public record ParticipantEntity(UUID groupId, UUID userId, String firstName, String lastName, String settlementStatus) {
}
