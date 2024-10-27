package com.sedeo.user.model;

import java.util.UUID;

public record FriendInvitation(UUID invitingUserId, UUID requestedUserId) {
}
