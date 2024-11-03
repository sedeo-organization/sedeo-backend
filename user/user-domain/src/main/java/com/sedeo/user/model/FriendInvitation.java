package com.sedeo.user.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.user.model.error.UserError.FriendInvitationStatusChangeNotAllowed;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.UUID;

public record FriendInvitation(UUID invitingUserId, UUID requestedUserId, InvitationStatus invitationStatus) {

    public Either<GeneralError, FriendInvitation> withChangedStatus(InvitationStatus newInvitationStatus) {
        return Option.of(this.invitationStatus)
                .filter(newInvitationStatus::isStatusChangePossible)
                .map(status -> new FriendInvitation(this.invitingUserId, this.requestedUserId, newInvitationStatus))
                .toEither(new FriendInvitationStatusChangeNotAllowed());
    }
}
