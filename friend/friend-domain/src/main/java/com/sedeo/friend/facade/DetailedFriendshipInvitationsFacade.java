package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.db.DetailedFriendshipInvitationRepository;
import com.sedeo.friend.model.DetailedFriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public class DetailedFriendshipInvitationsFacade implements DetailedFriendshipInvitations {

    private final DetailedFriendshipInvitationRepository detailedFriendshipInvitationRepository;

    public DetailedFriendshipInvitationsFacade(DetailedFriendshipInvitationRepository detailedFriendshipInvitationRepository) {
        this.detailedFriendshipInvitationRepository = detailedFriendshipInvitationRepository;
    }

    @Override
    public Either<GeneralError, List<DetailedFriendshipInvitation>> fetchDetailedFriendshipInvitations(UUID invitedUserId, InvitationStatus invitationStatus) {
        return detailedFriendshipInvitationRepository.find(invitedUserId, invitationStatus);
    }
}
