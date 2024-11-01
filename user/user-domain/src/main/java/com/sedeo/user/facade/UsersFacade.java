package com.sedeo.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.UserRepository;
import com.sedeo.user.db.model.FriendInvitationEntity;
import com.sedeo.user.model.FriendInvitation;
import com.sedeo.user.model.User;
import com.sedeo.user.model.UserMapper;
import com.sedeo.user.model.error.UserError.FriendsAlreadyExist;
import com.sedeo.user.model.error.UserError.UserNotFoundError;
import com.sedeo.user.model.error.UserError.FriendInvitationIsPending;
import io.vavr.control.Either;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sedeo.user.db.model.FriendInvitationEntity.InvitationStatus.PENDING;

public class UsersFacade implements Users {

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    public UsersFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<GeneralError, User> fetchUser(UUID userId) {
        return userRepository.findUser(userId)
                .filterOrElse(Objects::nonNull, error -> new UserNotFoundError())
                .map(USER_MAPPER::userEntityToUser);
    }

    @Override
    public Either<GeneralError, List<User>> fetchFriends(UUID userId) {
        return userRepository.findUsersFriends(userId)
                .map(USER_MAPPER::userEntityListToUser);
    }

    @Override
    public Either<GeneralError, List<User>> fetchFriendInvitationUsers(UUID userId) {
        return userRepository.findFriendInvitationUsers(userId)
                .map(USER_MAPPER::userEntityListToUser);
    }

    @Override
    public Either<GeneralError, List<User>> fetchUsersPotentialFriends(UUID userId, String searchPhrase) {
        return userRepository.findUsersPotentialFriends(userId, searchPhrase)
                .map(USER_MAPPER::userEntityListToUser);
    }

    @Override
    public Either<GeneralError, FriendInvitation> createFriendInvitation(UUID invitingUserId, UUID recipientUserId) {
        if (userRepository.friendsExist(invitingUserId, recipientUserId)) {
            return Either.left(new FriendsAlreadyExist());
        }

        //TODO: check if reverse invitation exists

        return userRepository.findFriendInvitation(invitingUserId, recipientUserId, PENDING)
                .filterOrElse(Objects::isNull, error -> new FriendInvitationIsPending())
                .flatMap(emptyInvitation -> userRepository.saveFriendInvitation(new FriendInvitationEntity(invitingUserId, recipientUserId, PENDING)))
                .map(USER_MAPPER::friendInvitationEntityToFriendInvitation);
    }
}
