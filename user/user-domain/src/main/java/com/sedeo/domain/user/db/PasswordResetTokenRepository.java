package com.sedeo.domain.user.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.user.model.PasswordResetToken;
import io.vavr.control.Either;

import java.util.UUID;

public interface PasswordResetTokenRepository {

    Either<GeneralError, PasswordResetToken> save(PasswordResetToken passwordResetToken);

    Either<GeneralError, PasswordResetToken> update(PasswordResetToken passwordResetToken);

    Either<GeneralError, PasswordResetToken> find(UUID token);
}
