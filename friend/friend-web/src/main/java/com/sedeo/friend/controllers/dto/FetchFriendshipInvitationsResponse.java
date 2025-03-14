package com.sedeo.friend.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchFriendshipInvitationsResponse(List<Invitation> invitations) {
    public record Invitation(
            UUID invitationId,
            UUID userId,
            String firstName,
            String lastName,
            String phoneNumber
    ) { }
}
