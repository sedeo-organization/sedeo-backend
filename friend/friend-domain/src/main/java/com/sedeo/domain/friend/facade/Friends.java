package com.sedeo.domain.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.Friend;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Friends {

    Either<GeneralError, Friend> createFriend(Friend friend);

    Either<GeneralError, List<Friend>> fetchFriends(List<UUID> userIds);

    Either<GeneralError, List<Friend>> fetchUsersPotentialFriends(UUID userId, String searchPhrase);
}
