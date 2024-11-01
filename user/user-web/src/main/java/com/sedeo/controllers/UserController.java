package com.sedeo.controllers;

import com.sedeo.controllers.dto.CreateFriendInvitationRequest;
import com.sedeo.controllers.dto.FetchPotentialFriendsRequest;
import com.sedeo.controllers.dto.UserControllerMapper;
import com.sedeo.user.facade.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private static final UserControllerMapper USER_CONTROLLER_MAPPER = UserControllerMapper.INSTANCE;
    private final Users users;

    public UserController(Users users) {
        this.users = users;
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> fetchUserProfile() {
        //TODO: Change UUID so that it is extracted from the token
        return users.fetchUser(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d")).fold(
                ResponseMapper::mapError,
                user -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.userToFetchUserProfileResponse(user))
        );
    }

    @GetMapping("users/friends")
    public ResponseEntity<?> fetchUserFriends() {
        //TODO: Change UUID so that it is extracted from the token
        return users.fetchFriends(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d")).fold(
                ResponseMapper::mapError,
                friends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendsResponse(friends))
        );
    }

    @GetMapping("users/friend-requests")
    public ResponseEntity<?> fetchUserFriendRequests() {
        //TODO: Change UUID so that it is extracted from the token
        return users.fetchFriendInvitationUsers(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d")).fold(
                ResponseMapper::mapError,
                invitingUsers -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendInvitationsResponse(invitingUsers))
        );
    }

    @GetMapping("users/potential-friends")
    public ResponseEntity<?> fetchUserPotentialFriends(@RequestParam(name = "search_phrase") FetchPotentialFriendsRequest fetchPotentialFriendsRequest) {
        //TODO: Change UUID so that it is extracted from the token
        return users.fetchUsersPotentialFriends(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d"), fetchPotentialFriendsRequest.searchPhrase()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchPotentialFriendsResponse(potentialFriends))
        );
    }

    @PostMapping("users/friend-requests")
    public ResponseEntity<?> createFriendRequest(@RequestBody CreateFriendInvitationRequest createFriendInvitationRequest) {
        //TODO: Change UUID so that it is extracted from the token
        return users.createFriendInvitation(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d"), createFriendInvitationRequest.recipientUserId()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }
}
