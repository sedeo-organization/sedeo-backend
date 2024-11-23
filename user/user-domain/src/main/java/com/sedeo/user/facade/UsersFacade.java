package com.sedeo.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.UserRepository;
import com.sedeo.user.db.model.FriendInvitationEntity;
import com.sedeo.user.db.model.FriendshipEntity;
import com.sedeo.user.model.*;
import com.sedeo.user.model.error.UserError.FriendsAlreadyExist;
import com.sedeo.user.model.error.UserError.UserNotFoundError;
import com.sedeo.user.model.error.UserError.FriendInvitationIsPending;
import io.vavr.control.Either;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Either<GeneralError, User> fetchUser(String email) {
        return userRepository.findUser(email)
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
        return userRepository.findFriendInvitationUsers(userId, PENDING)
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

    @Override
    @Transactional
    public Either<GeneralError, FriendInvitation> changeFriendInvitationStatus(UUID requestedUserId, UUID invitingUserId, InvitationStatus status) {
        Either<GeneralError, FriendInvitation> maybeFriendInvitation = userRepository.findFriendInvitation(invitingUserId, requestedUserId, PENDING)
                .map(USER_MAPPER::friendInvitationEntityToFriendInvitation)
                .flatMap(friendInvitation -> friendInvitation.withChangedStatus(status))
                .map(USER_MAPPER::friendInvitationToFriendInvitationEntity)
                .flatMap(userRepository::updateFriendInvitation)
                .map(USER_MAPPER::friendInvitationEntityToFriendInvitation);

        if (InvitationStatus.ACCEPTED.equals(status)) {
            maybeFriendInvitation.map(friendInvitation -> createFriendship(requestedUserId, invitingUserId).map(friendship -> friendInvitation));
        }

        return maybeFriendInvitation;
    }

    @Override
    public Either<GeneralError, Friendship> createFriendship(UUID firstUserId, UUID secondUserId) {
        if (userRepository.friendsExist(firstUserId, secondUserId)) {
            return Either.left(new FriendsAlreadyExist());
        }

        return userRepository.createFriendship(new FriendshipEntity(firstUserId, secondUserId))
                .map(USER_MAPPER::friendshipEntityToFriendship);
    }

    @Override
    public Either<GeneralError, List<User>> fetchUsers(List<UUID> userIds) {
        return userRepository.findUsers(userIds)
                .map(USER_MAPPER::userEntitiesToUsers);
    }

    @Override
    public Either<GeneralError, Void> addToUsersAccountBalance(UUID userId, BigDecimal positiveAmount) {
        BigDecimal forcedPositiveAmount = positiveAmount.abs();
        return this.fetchUser(userId)
                .map(user -> user.withAddedBalance(forcedPositiveAmount))
                .flatMap(user -> userRepository.updateUser(USER_MAPPER.userToUserEntity(user)))
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Either<GeneralError, Void> subtractFromUsersAccountBalance(UUID userId, BigDecimal positiveAmount) {
        BigDecimal forcedPositiveAmount = positiveAmount.abs();
        return this.fetchUser(userId)
                .map(user -> user.withReducedBalance(forcedPositiveAmount))
                .flatMap(user -> userRepository.updateUser(USER_MAPPER.userToUserEntity(user)))
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Boolean userExists(String email, String phoneNumber) {
        return userRepository.userExists(email, phoneNumber);
    }

    @Override
    public Either<GeneralError, Void> createUser(User user) {
        return userRepository.createUser(USER_MAPPER.userToUserEntity(user))
                .flatMap(result -> Either.right(null));
    }
}
