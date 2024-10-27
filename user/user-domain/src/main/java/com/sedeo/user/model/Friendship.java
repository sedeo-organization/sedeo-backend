package com.sedeo.user.model;

import java.util.UUID;

public record Friendship(UUID firstUserId, UUID secondUserId) {
}
