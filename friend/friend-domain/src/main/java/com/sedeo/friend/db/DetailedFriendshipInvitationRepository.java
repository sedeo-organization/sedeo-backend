package com.sedeo.friend.db;

import com.sedeo.friend.model.DetailedFriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface DetailedFriendshipInvitationRepository {

    Either<GeneralError, List<DetailedFriendshipInvitation>> find(UUID invitedUserId, InvitationStatus invitationStatus);
}
