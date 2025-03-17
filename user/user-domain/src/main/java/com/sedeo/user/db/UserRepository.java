package com.sedeo.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.model.User;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    Either<GeneralError, User> findUser(UUID userId);

    Either<GeneralError, List<User>> findUsers(List<UUID> userIds);

    Either<GeneralError, User> findUser(String email);

    Either<GeneralError, User> updateUser(User user);

    Boolean userExists(String email, String phoneNumber);

    Either<GeneralError, User> createUser(User user);
}
