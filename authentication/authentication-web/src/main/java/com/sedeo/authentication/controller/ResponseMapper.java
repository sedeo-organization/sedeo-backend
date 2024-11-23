package com.sedeo.authentication.controller;

import com.sedeo.authentication.model.error.UserAuthenticationError;
import com.sedeo.common.error.ErrorResponse;
import com.sedeo.common.error.GeneralError;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    private static final String USER_IS_NOT_AUTHENTICATED = "User is not authenticated";

    public static ResponseEntity<ErrorResponse> mapError(GeneralError error) {
        if (error instanceof UserAuthenticationError.EmailOrPasswordIncorrectError emailOrPasswordIncorrectError) {
            return ErrorResponse.unauthenticated(emailOrPasswordIncorrectError.message());
        } else if (error instanceof UserAuthenticationError.UserAlreadyExistsError userAlreadyExistsError) {
            return ErrorResponse.conflict(userAlreadyExistsError.message());
        } else {
            return ErrorResponse.unauthenticated(USER_IS_NOT_AUTHENTICATED);
        }
    }
}

