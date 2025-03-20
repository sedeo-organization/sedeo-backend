package com.sedeo.domain.friend.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.error.FriendError;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.UUID;

public record FriendshipInvitation(UUID invitationId, UUID invitingUserId, UUID invitedUserId, InvitationStatus invitationStatus) {

    public Either<GeneralError, FriendshipInvitation> withChangedStatus(InvitationStatus newInvitationStatus) {
        return Option.of(this.invitationStatus)
                .filter(newInvitationStatus::isStatusChangePossible)
                .map(status -> new FriendshipInvitation(this.invitationId, this.invitingUserId, this.invitedUserId, newInvitationStatus))
                .toEither(new FriendError.FriendInvitationStatusChangeNotAllowed());
    }
}
