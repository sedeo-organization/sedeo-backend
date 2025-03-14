package com.sedeo.friend.model;

import java.util.UUID;

public record DetailedFriendshipInvitation(UUID invitationId, Friend invitingUser, Friend invitedUser, InvitationStatus invitationStatus) {
}
