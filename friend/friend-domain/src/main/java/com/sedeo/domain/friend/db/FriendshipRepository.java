package com.sedeo.domain.friend.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.Friendship;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository {

    Boolean friendshipExists(UUID firstUserId, UUID secondUserId);

    Either<GeneralError, Friendship> save(Friendship friendship);

    Either<GeneralError, List<Friendship>> findUsersFriendships(UUID userId);
}
