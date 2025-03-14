package com.sedeo.friend.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.friend.db.FriendshipInvitationRepository;
import com.sedeo.friend.db.FriendshipRepository;
import com.sedeo.friend.model.FriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import com.sedeo.friend.model.error.FriendError;
import io.vavr.control.Either;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static com.sedeo.friend.model.InvitationStatus.PENDING;


public class FriendshipInvitationsFacade implements FriendshipInvitations {

    private final FriendshipInvitationRepository friendshipInvitationRepository;
    private final FriendshipRepository friendshipRepository;
    private final Friendships friendships;

    public FriendshipInvitationsFacade(FriendshipInvitationRepository friendshipInvitationRepository, FriendshipRepository friendshipRepository,
                                       Friendships friendships) {
        this.friendshipInvitationRepository = friendshipInvitationRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendships = friendships;
    }

    @Override
    public Either<GeneralError, FriendshipInvitation> createFriendshipInvitation(UUID invitingUserId, UUID invitedUserId) {
        if (friendshipRepository.friendshipExists(invitingUserId, invitedUserId)) {
            return Either.left(new FriendError.FriendsAlreadyExist());
        }

        //TODO: check if reverse invitation exists
        //TODO: check if user is not creating an invitation for himself

        return friendshipInvitationRepository.findByUserIds(invitingUserId, invitedUserId, PENDING)
                .filterOrElse(Objects::isNull, error -> new FriendError.FriendInvitationIsPending())
                .flatMap(emptyInvitation -> friendshipInvitationRepository.save(
                        new FriendshipInvitation(UUID.randomUUID(), invitingUserId, invitedUserId, PENDING))
                );
    }

    @Override
    @Transactional
    public Either<GeneralError, FriendshipInvitation> changeFriendshipInvitationStatus(UUID invitationId, UUID invitedUserId, InvitationStatus status) {
        Predicate<FriendshipInvitation> statusChangingUserIsTheOneBeingInvited = friendshipInvitation -> friendshipInvitation.invitedUserId().equals(invitedUserId);
        Either<GeneralError, FriendshipInvitation> maybeFriendshipInvitation = friendshipInvitationRepository.findById(invitationId, PENDING)
                .filterOrElse(Objects::nonNull, error -> new FriendError.FriendshipInvitationNotFound())
                .filterOrElse(statusChangingUserIsTheOneBeingInvited, error -> new FriendError.FriendInvitationStatusChangeNotAllowed())
                .flatMap(friendshipInvitation -> friendshipInvitation.withChangedStatus(status))
                .flatMap(friendshipInvitationRepository::update);

        if (InvitationStatus.ACCEPTED.equals(status)) {
            maybeFriendshipInvitation.map(friendshipInvitation -> friendships.createFriendship(friendshipInvitation.invitedUserId(), friendshipInvitation.invitingUserId())
                    .map(friendship -> friendshipInvitation));
        }

        return maybeFriendshipInvitation;
    }
}
