package com.sedeo.user.model.error;

import com.sedeo.common.error.DomainError;

public interface UserError extends DomainError {

    String USER_WAS_NOT_FOUND = "User was not found";
    String FRIENDS_ALREADY_EXIST = "Friends already exist";
    String FRIEND_INVITATION_IS_PENDING = "Friend invitation is pending";

    record UserNotFoundError(String message) implements UserError {
        public UserNotFoundError() {
            this(USER_WAS_NOT_FOUND);
        }
    }

    record FriendsAlreadyExist(String message) implements UserError {
        public FriendsAlreadyExist() {
            this(FRIENDS_ALREADY_EXIST);
        }
    }

    record FriendInvitationIsPending(String message) implements UserError {
        public FriendInvitationIsPending() {
            this(FRIEND_INVITATION_IS_PENDING);
        }
    }
}
