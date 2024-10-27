package com.sedeo.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.model.User;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Users {

    Either<GeneralError, User> fetchUser(UUID userId);

    Either<GeneralError, List<User>> fetchFriends(UUID userId);

    Either<GeneralError, List<User>> fetchFriendInvitationUsers(UUID userId);
}
