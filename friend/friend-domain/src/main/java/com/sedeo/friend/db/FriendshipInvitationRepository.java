package com.sedeo.friend.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.model.FriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface FriendshipInvitationRepository {

    Either<GeneralError, FriendshipInvitation> save(FriendshipInvitation friendshipInvitation);

    Either<GeneralError, FriendshipInvitation> update(FriendshipInvitation friendInvitationEntity);

    Either<GeneralError, FriendshipInvitation> findByUserIds(UUID invitingUserId, UUID invitedUserId, InvitationStatus invitationStatus);

    Either<GeneralError, List<FriendshipInvitation>> findUsersIncomingFriendshipInvitationsByUserId(UUID invitedUserId, InvitationStatus invitationStatus);

    Either<GeneralError, FriendshipInvitation> findById(UUID invitationId, InvitationStatus invitationStatus);

    Boolean existsByUserIds(UUID invitingUser, UUID invitedUserId);
}
