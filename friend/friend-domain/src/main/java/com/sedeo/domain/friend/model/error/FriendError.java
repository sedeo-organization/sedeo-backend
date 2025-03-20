package com.sedeo.domain.friend.model.error;

import com.sedeo.common.error.DomainError;

public interface FriendError extends DomainError {

    String FRIENDS_ALREADY_EXIST = "Friends already exist";
    String FRIEND_INVITATION_IS_PENDING = "Friend invitation is pending";
    String FRIEND_INVITATION_STATUS_CHANGE_NOT_ALLOWED = "Friend invitation status change is not allowed";
    String FRIEND_ALREADY_EXISTS = "Friend already exists";
    String FRIENDSHIP_INVITATION_WAS_NOT_FOUND = "Friendship invitation was not found";

    record FriendsAlreadyExist(String message) implements FriendError {
        public FriendsAlreadyExist() {
            this(FRIENDS_ALREADY_EXIST);
        }
    }

    record FriendInvitationIsPending(String message) implements FriendError {
        public FriendInvitationIsPending() {
            this(FRIEND_INVITATION_IS_PENDING);
        }
    }

    record FriendInvitationStatusChangeNotAllowed(String message) implements FriendError {
        public FriendInvitationStatusChangeNotAllowed() {
            this(FRIEND_INVITATION_STATUS_CHANGE_NOT_ALLOWED);
        }
    }

    record FriendAlreadyExists(String message) implements FriendError {
        public FriendAlreadyExists() {
            this(FRIEND_ALREADY_EXISTS);
        }
    }

    record FriendshipInvitationNotFound(String message) implements FriendError {
        public FriendshipInvitationNotFound() {
            this(FRIENDSHIP_INVITATION_WAS_NOT_FOUND);
        }
    }
}
