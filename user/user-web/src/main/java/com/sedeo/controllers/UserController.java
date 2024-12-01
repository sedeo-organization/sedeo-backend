package com.sedeo.controllers;

import com.sedeo.cloudapi.azure.email.Mail;
import com.sedeo.cloudapi.azure.email.model.MailRequest;
import com.sedeo.cloudapi.azure.email.model.ResetPasswordMessageTemplate;
import com.sedeo.controllers.dto.*;
import com.sedeo.user.facade.Users;
import com.sedeo.user.model.InvitationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
public class UserController {

    private static final UserControllerMapper USER_CONTROLLER_MAPPER = UserControllerMapper.INSTANCE;
    private static final String PASSWORD_RESET_REQUEST = "Password reset request";

    private final Users users;
    private final Mail mail;
    private final ResetPasswordMessageTemplate resetPasswordMessageTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserController(Users users, Mail mail, ResetPasswordMessageTemplate resetPasswordMessageTemplate, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.mail = mail;
        this.resetPasswordMessageTemplate = resetPasswordMessageTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> fetchUserProfile(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchUser(userId).fold(
                ResponseMapper::mapError,
                user -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.userToFetchUserProfileResponse(user))
        );
    }

    @GetMapping("/users/friends")
    public ResponseEntity<?> fetchUserFriends(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchFriends(userId).fold(
                ResponseMapper::mapError,
                friends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendsResponse(friends))
        );
    }

    @GetMapping("/users/friend-requests")
    public ResponseEntity<?> fetchUserFriendRequests(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchFriendInvitationUsers(userId).fold(
                ResponseMapper::mapError,
                invitingUsers -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchFriendInvitationsResponse(invitingUsers))
        );
    }

    @GetMapping("/users/potential-friends")
    public ResponseEntity<?> fetchUserPotentialFriends(@RequestParam(name = "search_phrase") FetchPotentialFriendsRequest fetchPotentialFriendsRequest,
                                                       Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.fetchUsersPotentialFriends(userId, fetchPotentialFriendsRequest.searchPhrase()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.ok(USER_CONTROLLER_MAPPER.usersToFetchPotentialFriendsResponse(potentialFriends))
        );
    }

    @PostMapping("/users/friend-requests")
    public ResponseEntity<?> createFriendRequest(@RequestBody CreateFriendInvitationRequest createFriendInvitationRequest,
                                                 Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return users.createFriendInvitation(userId, createFriendInvitationRequest.recipientUserId()).fold(
                ResponseMapper::mapError,
                potentialFriends -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }

    @PatchMapping("/users/friend-requests")
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

    @PostMapping("/users/password")
    public ResponseEntity<?> createPasswordResetToken(@RequestBody PasswordResetTokenRequest passwordResetTokenRequest) {
        return users.createPasswordResetToken(passwordResetTokenRequest.email())
                .fold(
                        ResponseMapper::mapError,
                        passwordResetToken -> {
                            resetPasswordMessageTemplate.withResetPasswordMessage(
                                            passwordResetToken.firstName(), passwordResetToken.lastName(), passwordResetToken.token().toString())
                                    .ifPresent(message -> mail.sendMail(new MailRequest(passwordResetToken.email(), PASSWORD_RESET_REQUEST, message)));
                            return ResponseEntity.status(HttpStatus.CREATED).build();
                        }
                );
    }

    @PatchMapping("/users/password")
    public ResponseEntity<?> resetUsersPassword(@RequestBody ResetUsersPasswordRequest resetUsersPasswordRequest) {
        return users.changeUsersPassword(resetUsersPasswordRequest.token(), passwordEncoder.encode(resetUsersPasswordRequest.password()))
                .fold(
                        ResponseMapper::mapError,
                        success -> ResponseEntity.status(HttpStatus.OK).build()
                );
    }
}
