package com.sedeo.friend.controllers;

import com.sedeo.common.error.ErrorResponse;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.error.FriendError;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    private static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred";

    public static ResponseEntity<ErrorResponse> mapError(GeneralError error) {
        if (error instanceof FriendError.FriendInvitationStatusChangeNotAllowed domainError) {
            return ErrorResponse.conflict(domainError.message());
        } else if (error instanceof FriendError.FriendInvitationIsPending domainError) {
            return ErrorResponse.conflict(domainError.message());
        } else if (error instanceof FriendError.FriendsAlreadyExist domainError) {
            return ErrorResponse.conflict(domainError.message());
        } else if (error instanceof FriendError.FriendshipInvitationNotFound domainError) {
            return ErrorResponse.notFound(domainError.message());
        } else {
            return ErrorResponse.databaseError(UNEXPECTED_ERROR_OCCURRED);
        }
    }
}
