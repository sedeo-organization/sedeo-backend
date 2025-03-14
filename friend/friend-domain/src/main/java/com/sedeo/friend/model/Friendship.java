package com.sedeo.friend.model;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Friendship(UUID friendshipId, UUID firstUserId, UUID secondUserId) {

    public static List<UUID> extractFriendIds(List<Friendship> friendships, UUID userId) {
        Predicate<UUID> isNotUserId = id -> !id.equals(userId);
        return friendships.stream()
                .flatMap(friendship -> Stream.of(friendship.firstUserId, friendship.secondUserId).filter(isNotUserId))
                .toList();
    }

    public static Friendship withRandomId(UUID firstUserId, UUID secondUserId) {
        return new Friendship(UUID.randomUUID(), firstUserId, secondUserId);
    }
}
