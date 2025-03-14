package com.sedeo.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.model.UserEntity;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    Either<GeneralError, UserEntity> findUser(UUID userId);

    Either<GeneralError, List<UserEntity>> findUsers(List<UUID> userIds);

    Either<GeneralError, UserEntity> findUser(String email);

    Either<GeneralError, UserEntity> updateUser(UserEntity userEntity);

    Boolean userExists(String email, String phoneNumber);

    Either<GeneralError, UserEntity> createUser(UserEntity userEntity);
}
