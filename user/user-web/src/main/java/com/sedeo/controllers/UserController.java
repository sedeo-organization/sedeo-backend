package com.sedeo.controllers;

import com.sedeo.controllers.dto.UserControllerMapper;
import com.sedeo.user.facade.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
