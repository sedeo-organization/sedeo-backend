package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.DetailedFriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface DetailedFriendshipInvitations {

    Either<GeneralError, List<DetailedFriendshipInvitation>> fetchDetailedFriendshipInvitations(UUID invitedUserId, InvitationStatus invitationStatus);
}
