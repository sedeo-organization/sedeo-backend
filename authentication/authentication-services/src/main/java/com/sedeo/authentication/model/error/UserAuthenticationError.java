package com.sedeo.authentication.model.error;

import com.sedeo.common.error.DomainError;

public interface UserAuthenticationError extends DomainError {

    String USER_WITH_THAT_EMAIL_OR_PHONE_NUMBER_ALREADY_EXISTS = "User with that email or phone number already exists";
    String EMAIL_OR_PASSWORD_INCORRECT = "Email or password incorrect";

    record UserAlreadyExistsError(String message) implements UserAuthenticationError {
        public UserAlreadyExistsError() {
            this(USER_WITH_THAT_EMAIL_OR_PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    record EmailOrPasswordIncorrectError(String message) implements UserAuthenticationError {
        public EmailOrPasswordIncorrectError() {
            this(EMAIL_OR_PASSWORD_INCORRECT);
        }
    }
}
