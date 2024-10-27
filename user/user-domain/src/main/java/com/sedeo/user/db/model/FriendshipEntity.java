package com.sedeo.user.db.model;

import java.util.UUID;

public record FriendshipEntity(UUID firstUserId, UUID secondUserId) {
}
