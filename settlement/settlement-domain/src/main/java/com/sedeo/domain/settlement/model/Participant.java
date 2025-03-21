package com.sedeo.domain.settlement.model;

import java.util.UUID;

public record Participant(UUID groupId, UUID userId, String firstName, String lastName, SettlementStatus status) {

    public Participant withPendingStatus() {
        return new Participant(this.groupId, this.userId, this.firstName, this.lastName, SettlementStatus.PENDING);
    }

    public Participant withSettledStatus() {
        return new Participant(this.groupId, this.userId, this.firstName, this.lastName, SettlementStatus.SETTLED);
    }

    public Participant withStatus(SettlementStatus newStatus) {
        return new Participant(this.groupId, this.userId, this.firstName, this.lastName, newStatus);
    }
}
