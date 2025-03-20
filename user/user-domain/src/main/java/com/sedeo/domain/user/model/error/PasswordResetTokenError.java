package com.sedeo.domain.user.model.error;

import com.sedeo.common.error.DomainError;

public interface PasswordResetTokenError extends DomainError {

    String TOKEN_IS_EXPIRED = "Time reserved for password change has already passed";
    String PASSWORD_HAS_BEEN_CHANGED_ALREADY = "Password has been changed already";

    record PasswordChangeTimePassed(String message) implements PasswordResetTokenError {
        public PasswordChangeTimePassed() {
            this(TOKEN_IS_EXPIRED);
        }
    }

    record TokenUsedAlready(String message) implements PasswordResetTokenError {
        public TokenUsedAlready() {
            this(PASSWORD_HAS_BEEN_CHANGED_ALREADY);
        }
    }
}
