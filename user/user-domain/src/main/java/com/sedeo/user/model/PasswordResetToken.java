package com.sedeo.user.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.model.error.PasswordResetTokenError;
import io.vavr.control.Either;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.sedeo.user.model.TokenStatus.UNUSED;
import static com.sedeo.user.model.TokenStatus.USED;

public record PasswordResetToken(UUID token, UUID userId, String firstName, String lastName, String email,
                                 LocalDateTime expirationTime, TokenStatus tokenStatus) {


    private static final int EXPIRATION_TIME_IN_MINUTES = 5;

    public static PasswordResetToken randomUnusedToken(UUID userId, String firstName, String lastName, String email) {
        return new PasswordResetToken(UUID.randomUUID(), userId, firstName, lastName, email, LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES), UNUSED);
    }

    public Either<GeneralError, PasswordResetToken> useToken() {
        if (this.isExpired()) {
            return Either.left(new PasswordResetTokenError.PasswordChangeTimePassed());
        }
        return markTokenAsUsed();
    }

    private Either<GeneralError, PasswordResetToken> markTokenAsUsed() {
        if (this.isUsed()) {
            return Either.left(new PasswordResetTokenError.TokenUsedAlready());
        }
        return Either.right(this.usedToken());
    }

    private Boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }

    private Boolean isUsed() {
        return USED.equals(this.tokenStatus);
    }

    private PasswordResetToken usedToken() {
        return new PasswordResetToken(this.token, this.userId, this.firstName, this.lastName, this.email, this.expirationTime, USED);
    }
}
