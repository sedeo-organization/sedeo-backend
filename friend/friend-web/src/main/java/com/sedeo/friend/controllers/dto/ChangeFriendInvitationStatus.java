package com.sedeo.friend.controllers.dto;

public record ChangeFriendInvitationStatus(FriendInvitationStatus status) {
    public enum FriendInvitationStatus {
        ACCEPTED,
        REJECTED
    }
}
