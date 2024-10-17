package com.sedeo.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(Integer statusCode, String message) {

    public static ResponseEntity<ErrorResponse> notFound(String message) {
        return new ResponseEntity<>(new ErrorResponse(404, message), HttpStatus.NOT_FOUND);
    }
    
    public static ResponseEntity<ErrorResponse> databaseError(String message) {
        return new ResponseEntity<>(new ErrorResponse(409, message), HttpStatus.CONFLICT);
    }
}
