package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.FriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import io.vavr.control.Either;

import java.util.UUID;

public interface FriendshipInvitations {

    Either<GeneralError, FriendshipInvitation> createFriendshipInvitation(UUID invitingUserId, UUID invitedUserId);

    Either<GeneralError, FriendshipInvitation> changeFriendshipInvitationStatus(UUID invitationId, UUID invitedUserId, InvitationStatus status);

}
