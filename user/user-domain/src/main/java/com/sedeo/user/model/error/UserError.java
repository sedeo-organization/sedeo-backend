package com.sedeo.user.model.error;

import com.sedeo.common.error.DomainError;

public interface UserError extends DomainError {

    String USER_WAS_NOT_FOUND = "User was not found";

    record UserNotFoundError(String message) implements UserError {
        public UserNotFoundError() {
            this(USER_WAS_NOT_FOUND);
        }
    }
}
