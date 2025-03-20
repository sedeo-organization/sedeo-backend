package com.sedeo.domain.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.db.FriendshipRepository;
import com.sedeo.domain.friend.model.Friendship;
import com.sedeo.domain.friend.model.error.FriendError;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public class FriendshipsFacade implements Friendships {

    private final FriendshipRepository friendshipRepository;

    public FriendshipsFacade(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public Either<GeneralError, Friendship> createFriendship(UUID firstUserId, UUID secondUserId) {
        if (friendshipRepository.friendshipExists(firstUserId, secondUserId)) {
            return Either.left(new FriendError.FriendsAlreadyExist());
        }

        return friendshipRepository.save(Friendship.withRandomId(firstUserId, secondUserId));
    }

    @Override
    public Either<GeneralError, List<Friendship>> fetchUsersFriendships(UUID userId) {
        return friendshipRepository.findUsersFriendships(userId);
    }

}
