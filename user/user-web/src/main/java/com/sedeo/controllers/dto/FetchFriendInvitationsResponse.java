package com.sedeo.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchFriendInvitationsResponse(List<InvitingUser> invitingUsers) {
    public record InvitingUser(
            UUID userId,
            String firstName,
            String lastName,
            String phoneNumber
    ) { }
}
