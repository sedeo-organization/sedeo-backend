package com.sedeo.user.db.model;

import java.util.UUID;

public record FriendInvitationEntity(UUID invitingUserId, UUID requestedUserId, InvitationStatus invitationStatus) {
    public enum InvitationStatus {
        ACCEPTED,
        PENDING,
        REJECTED
    }
}
