package com.sedeo.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.model.*;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface Users {

    Either<GeneralError, User> fetchUser(UUID userId);

    Either<GeneralError, User> fetchUser(String email);

    Either<GeneralError, List<User>> fetchFriends(UUID userId);

    Either<GeneralError, List<User>> fetchFriendInvitationUsers(UUID userId);

    Either<GeneralError, List<User>> fetchUsersPotentialFriends(UUID userId, String searchPhrase);

    Either<GeneralError, FriendInvitation> createFriendInvitation(UUID invitingUserId, UUID recipientUserId);

    Either<GeneralError, FriendInvitation> changeFriendInvitationStatus(UUID requestedUserId, UUID invitingUserId, InvitationStatus status);

    Either<GeneralError, Friendship> createFriendship(UUID firstUserId, UUID secondUserId);

    Either<GeneralError, List<User>> fetchUsers(List<UUID> userIds);

    Either<GeneralError, Void> addToUsersAccountBalance(UUID userId, BigDecimal positiveAmount);

    Either<GeneralError, Void> subtractFromUsersAccountBalance(UUID userId, BigDecimal positiveAmount);

    Boolean userExists(String email, String phoneNumber);

    Either<GeneralError, Void> createUser(User user);

    Either<GeneralError, PasswordResetToken> createPasswordResetToken(String email);

    Either<GeneralError, User> changeUsersPassword(UUID token, String newPassword);
}
