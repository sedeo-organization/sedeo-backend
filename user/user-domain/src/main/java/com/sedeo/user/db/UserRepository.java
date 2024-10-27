package com.sedeo.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.model.UserEntity;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    Either<GeneralError, UserEntity> findUser(UUID userId);

    Either<GeneralError, List<UserEntity>> findUsers(List<UUID> userIds);

    Either<GeneralError, List<UserEntity>> findUsersFriends(UUID userId);

    Either<GeneralError, List<UserEntity>> findFriendInvitationUsers(UUID userId);
}
