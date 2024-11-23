package com.sedeo.controllers;

import com.sedeo.controllers.dto.ChangeFriendInvitationStatus;
import com.sedeo.controllers.dto.CreateFriendInvitationRequest;
import com.sedeo.controllers.dto.FetchPotentialFriendsRequest;
import com.sedeo.controllers.dto.UserControllerMapper;
import com.sedeo.user.facade.Users;
import com.sedeo.user.model.InvitationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
public class UserController {

    private static final UserControllerMapper USER_CONTROLLER_MAPPER = UserControllerMapper.INSTANCE;
    private final Users users;

    public UserController(Users users) {
        this.users = users;
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> fetchUserProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchUser(userId).fold(
                ResponseMapper::mapError,
                user -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.userToFetchUserProfileResponse(user))
        );
    }

    @GetMapping("users/friends")
    public ResponseEntity<?> fetchUserFriends(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchFriends(userId).fold(
                ResponseMapper::mapError,
                friends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendsResponse(friends))
        );
    }

    @GetMapping("users/friend-requests")
    public ResponseEntity<?> fetchUserFriendRequests(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchFriendInvitationUsers(userId).fold(
                ResponseMapper::mapError,
                invitingUsers -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendInvitationsResponse(invitingUsers))
        );
    }

    @GetMapping("users/potential-friends")
    public ResponseEntity<?> fetchUserPotentialFriends(@RequestParam(name = "search_phrase") FetchPotentialFriendsRequest fetchPotentialFriendsRequest,
                                                       Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchUsersPotentialFriends(userId, fetchPotentialFriendsRequest.searchPhrase()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchPotentialFriendsResponse(potentialFriends))
        );
    }

    @PostMapping("users/friend-requests")
    public ResponseEntity<?> createFriendRequest(@RequestBody CreateFriendInvitationRequest createFriendInvitationRequest,
                                                 Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.createFriendInvitation(userId, createFriendInvitationRequest.recipientUserId()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }

    @PatchMapping("users/friend-requests")
    public ResponseEntity<?> acceptOrRejectFriendInvitation(@RequestBody ChangeFriendInvitationStatus changeFriendInvitationStatus,
                                                            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.changeFriendInvitationStatus(userId,
                changeFriendInvitationStatus.invitingUserId(),
                InvitationStatus.valueOf(changeFriendInvitationStatus.status().toString())).fold(
                        ResponseMapper::mapError,
                        potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }
}
