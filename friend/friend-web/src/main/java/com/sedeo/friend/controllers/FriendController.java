package com.sedeo.friend.controllers;

import com.sedeo.friend.controllers.dto.ChangeFriendInvitationStatus;
import com.sedeo.friend.controllers.dto.CreateFriendshipInvitationRequest;
import com.sedeo.friend.controllers.dto.FetchPotentialFriendsRequest;
import com.sedeo.friend.controllers.dto.FriendControllerMapper;
import com.sedeo.domain.friend.facade.DetailedFriendshipInvitations;
import com.sedeo.domain.friend.facade.Friends;
import com.sedeo.domain.friend.facade.FriendshipInvitations;
import com.sedeo.domain.friend.facade.Friendships;
import com.sedeo.domain.friend.model.Friendship;
import com.sedeo.domain.friend.model.InvitationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
public class FriendController {

    private static final FriendControllerMapper FRIEND_CONTROLLER_MAPPER = FriendControllerMapper.INSTANCE;

    private final Friends friends;
    private final Friendships friendships;
    private final FriendshipInvitations friendshipInvitations;
    private final DetailedFriendshipInvitations detailedFriendshipInvitations;

    public FriendController(Friends friends, Friendships friendships, FriendshipInvitations friendshipInvitations,
                            DetailedFriendshipInvitations detailedFriendshipInvitations) {
        this.friends = friends;
        this.friendships = friendships;
        this.friendshipInvitations = friendshipInvitations;
        this.detailedFriendshipInvitations = detailedFriendshipInvitations;
    }

    @GetMapping("/friends")
    public ResponseEntity<?> fetchUsersFriends(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        return friendships.fetchUsersFriendships(userId)
                .map(foundFriendships -> Friendship.extractFriendIds(foundFriendships, userId))
                .flatMap(friends::fetchFriends)
                .fold(
                        ResponseMapper::mapError,
                        friends -> ResponseEntity.ok(FRIEND_CONTROLLER_MAPPER.friendsToFetchFriendsResponse(friends))
                );
    }

    @GetMapping("/friendship-invitations")
    public ResponseEntity<?> fetchUsersFriendshipInvitations(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());

        return detailedFriendshipInvitations.fetchDetailedFriendshipInvitations(userId, InvitationStatus.PENDING).fold(
                ResponseMapper::mapError,
                friendshipInvitations -> ResponseEntity.ok(FRIEND_CONTROLLER_MAPPER.detailedFriendshipInvitationsToFetchFriendshipInvitationResponse(friendshipInvitations))
        );
    }

    @GetMapping("/potential-friends")
    public ResponseEntity<?> fetchUserPotentialFriends(@RequestParam(name = "search_phrase") FetchPotentialFriendsRequest fetchPotentialFriendsRequest,
                                                       Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return friends.fetchUsersPotentialFriends(userId, fetchPotentialFriendsRequest.searchPhrase()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.ok(FRIEND_CONTROLLER_MAPPER.friendsToFetchPotentialFriendsResponse(potentialFriends))
        );
    }

    @PostMapping("/friendship-invitations")
    public ResponseEntity<?> createFriendshipInvitation(@RequestBody CreateFriendshipInvitationRequest createFriendshipInvitationRequest,
                                                 Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return friendshipInvitations.createFriendshipInvitation(userId, createFriendshipInvitationRequest.invitedUserId()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }

    @PatchMapping("/friendship-invitations/{friendshipInvitationId}")
    public ResponseEntity<?> acceptOrRejectFriendInvitation(@RequestBody ChangeFriendInvitationStatus changeFriendInvitationStatus,
                                                            Principal principal, @PathVariable UUID friendshipInvitationId) {
        UUID userId = UUID.fromString(principal.getName());
        return friendshipInvitations.changeFriendshipInvitationStatus(friendshipInvitationId, userId,
                InvitationStatus.valueOf(changeFriendInvitationStatus.status().toString())).fold(
                    ResponseMapper::mapError,
                    potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }
}
