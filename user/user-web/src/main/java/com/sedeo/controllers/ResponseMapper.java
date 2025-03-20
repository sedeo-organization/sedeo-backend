package com.sedeo.controllers;

import com.sedeo.common.error.ErrorResponse;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.user.model.error.UserError.UserNotFoundError;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    private static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred";

    public static ResponseEntity<ErrorResponse> mapError(GeneralError error) {
        if (error instanceof UserNotFoundError domainError) {
            return ErrorResponse.notFound(domainError.message());
        } else {
            return ErrorResponse.databaseError(UNEXPECTED_ERROR_OCCURRED);
        }  
    }
}
