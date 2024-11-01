package com.sedeo.controllers.dto;

import java.util.UUID;

public record CreateFriendInvitationRequest(UUID recipientUserId) {
}
