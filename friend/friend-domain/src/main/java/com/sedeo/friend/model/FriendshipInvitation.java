package com.sedeo.friend.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.error.FriendError.FriendInvitationStatusChangeNotAllowed;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;
import java.util.UUID;

public record FriendshipInvitation(UUID invitationId, UUID invitingUserId, UUID invitedUserId, InvitationStatus invitationStatus) {

    public Either<GeneralError, FriendshipInvitation> withChangedStatus(InvitationStatus newInvitationStatus) {
        return Option.of(this.invitationStatus)
                .filter(newInvitationStatus::isStatusChangePossible)
                .map(status -> new FriendshipInvitation(this.invitationId, this.invitingUserId, this.invitedUserId, newInvitationStatus))
                .toEither(new FriendInvitationStatusChangeNotAllowed());
    }
}
