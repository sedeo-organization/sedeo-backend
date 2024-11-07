package com.sedeo.settlement.controllers;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import org.springframework.http.ResponseEntity;
import com.sedeo.common.error.ErrorResponse;

public class ResponseMapper {

    private static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred";

    public static ResponseEntity<ErrorResponse> mapError(GeneralError error) {
        if (error instanceof DatabaseError databaseError) {
            return ErrorResponse.databaseError(UNEXPECTED_ERROR_OCCURRED);
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
