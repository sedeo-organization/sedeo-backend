package com.sedeo.controllers.dto;

import java.util.UUID;

public record ChangeFriendInvitationStatus(UUID invitingUserId, FriendInvitationStatus status) {
    public enum FriendInvitationStatus {
        ACCEPTED,
        REJECTED
    }
}
