package com.sedeo.user.model;

public enum InvitationStatus {
    PENDING,
    ACCEPTED(PENDING),
    REJECTED(PENDING);

    private final InvitationStatus allowedStatus;

    InvitationStatus() {
        this.allowedStatus = null;
    }

    InvitationStatus(InvitationStatus allowedStatus) {
        this.allowedStatus = allowedStatus;
    }

    public boolean isStatusChangePossible(InvitationStatus status) {
        return status.equals(this.allowedStatus);
    }
}