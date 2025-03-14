package com.sedeo.controllers;

import com.sedeo.cloudapi.azure.email.Mail;
import com.sedeo.cloudapi.azure.email.model.MailRequest;
import com.sedeo.cloudapi.azure.email.model.ResetPasswordMessageTemplate;
import com.sedeo.controllers.dto.PasswordResetTokenRequest;
import com.sedeo.controllers.dto.ResetUsersPasswordRequest;
import com.sedeo.controllers.dto.UserControllerMapper;
import com.sedeo.user.facade.Users;
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
