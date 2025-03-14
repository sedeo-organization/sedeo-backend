package com.sedeo.friend.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchFriendsResponse(List<Friend> friends) {
    public record Friend(
            UUID userId,
            String firstName,
            String lastName,
            String phoneNumber
    ) { }
}
