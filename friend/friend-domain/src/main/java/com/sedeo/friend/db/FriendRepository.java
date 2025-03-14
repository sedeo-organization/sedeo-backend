package com.sedeo.friend.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.Friend;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface FriendRepository {

    Either<GeneralError, Friend> save(Friend friend);

    boolean friendExistsById(UUID userId);

    Either<GeneralError, List<Friend>> findFriendsByIds(List<UUID> userIds);

    Either<GeneralError, List<Friend>> findUsersPotentialFriends(UUID userId, String searchPhrase);
}
