package com.sedeo.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.model.UserEntity;
import io.vavr.control.Either;

import java.util.UUID;

public interface UserRepository {

    Either<GeneralError, UserEntity> find(UUID userId);
}
