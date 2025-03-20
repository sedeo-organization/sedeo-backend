package com.sedeo.friend.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchPotentialFriendsResponse(List<PotentialFriend> potentialFriends) {
    public record PotentialFriend(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber
    ) { }
}
