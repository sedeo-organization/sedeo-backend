package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.db.FriendRepository;
import com.sedeo.friend.model.Friend;
import com.sedeo.friend.model.error.FriendError;
import io.vavr.control.Either;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FriendsFacade implements Friends {

    private final FriendRepository friendRepository;

    public FriendsFacade(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public Either<GeneralError, List<Friend>> fetchUsersPotentialFriends(UUID userId, String searchPhrase) {
        return friendRepository.findUsersPotentialFriends(userId, searchPhrase);
    }

    @Override
    public Either<GeneralError, Friend> createFriend(Friend friend) {
        if (friendRepository.friendExistsById(friend.userId())) {
            return Either.left(new FriendError.FriendAlreadyExists());
        }
        return friendRepository.save(friend);
    }

    @Override
    public Either<GeneralError, List<Friend>> fetchFriends(List<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Either.right(Collections.emptyList());
        }
        return friendRepository.findFriendsByIds(userIds);
    }
}
