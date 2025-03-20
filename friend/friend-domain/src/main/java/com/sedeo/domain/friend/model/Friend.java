package com.sedeo.domain.friend.model;

import java.util.UUID;

public record Friend(UUID userId, String firstName, String lastName, String phoneNumber) {
}
