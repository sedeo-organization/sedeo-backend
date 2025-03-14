package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.Friendship;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Friendships {

    Either<GeneralError, Friendship> createFriendship(UUID firstUserId, UUID secondUserId);

    Either<GeneralError, List<Friendship>> fetchUsersFriendships(UUID userId);

}
