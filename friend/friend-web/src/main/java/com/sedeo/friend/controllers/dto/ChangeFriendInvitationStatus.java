package com.sedeo.friend.controllers.dto;

import java.util.UUID;

public record ChangeFriendInvitationStatus(FriendInvitationStatus status) {
    public enum FriendInvitationStatus {
        ACCEPTED,
        REJECTED
    }
}
