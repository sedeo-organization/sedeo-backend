package com.sedeo.domain.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.model.DetailedFriendshipInvitation;
import com.sedeo.domain.friend.model.InvitationStatus;
import com.sedeo.domain.friend.db.DetailedFriendshipInvitationRepository;
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
