package com.sedeo.domain.friend.db;

import com.sedeo.domain.friend.model.DetailedFriendshipInvitation;
import com.sedeo.domain.friend.model.InvitationStatus;
import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface DetailedFriendshipInvitationRepository {

    Either<GeneralError, List<DetailedFriendshipInvitation>> find(UUID invitedUserId, InvitationStatus invitationStatus);
}
