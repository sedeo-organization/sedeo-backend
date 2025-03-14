package com.sedeo.friend.controllers.dto;

import java.util.UUID;

public record CreateFriendshipInvitationRequest(UUID invitedUserId) {
}
