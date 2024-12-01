package com.sedeo.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.model.PasswordResetTokenEntity;
import io.vavr.control.Either;

import java.util.UUID;

public interface PasswordResetTokenRepository {

    Either<GeneralError, PasswordResetTokenEntity> save(PasswordResetTokenEntity passwordResetToken);

    Either<GeneralError, PasswordResetTokenEntity> update(PasswordResetTokenEntity passwordResetToken);

    Either<GeneralError, PasswordResetTokenEntity> find(UUID token);
}
