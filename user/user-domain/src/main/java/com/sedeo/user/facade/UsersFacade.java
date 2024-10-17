package com.sedeo.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.UserRepository;
import com.sedeo.user.model.User;
import com.sedeo.user.model.UserMapper;
import com.sedeo.user.model.error.UserError.UserNotFoundError;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.UUID;

public class UsersFacade implements Users {

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    public UsersFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<GeneralError, User> fetchUser(UUID userId) {
        return userRepository.find(userId)
                .filterOrElse(Objects::nonNull, error -> new UserNotFoundError())
                .map(USER_MAPPER::userEntityToUser);
    }

}
