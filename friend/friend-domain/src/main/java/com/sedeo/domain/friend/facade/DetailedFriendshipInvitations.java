package com.sedeo.domain.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.DetailedFriendshipInvitation;
import com.sedeo.domain.friend.model.InvitationStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface DetailedFriendshipInvitations {

    Either<GeneralError, List<DetailedFriendshipInvitation>> fetchDetailedFriendshipInvitations(UUID invitedUserId, InvitationStatus invitationStatus);
}
