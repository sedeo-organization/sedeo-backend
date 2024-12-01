package com.sedeo.authentication.controller;

import com.sedeo.authentication.controller.dto.LoginRequest;
import com.sedeo.authentication.controller.dto.LoginResponse;
import com.sedeo.authentication.controller.dto.RegisterRequest;
import com.sedeo.authentication.model.AuthenticateUserServiceRequest;
import com.sedeo.authentication.model.RegisterUserServiceRequest;
import com.sedeo.authentication.services.UserAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    public AuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        return userAuthenticationService.register(new RegisterUserServiceRequest(
                registerRequest.email(),
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.phoneNumber(),
                registerRequest.password()
        )).fold(
                ResponseMapper::mapError,
                success -> ResponseEntity.status(HttpStatus.CREATED).build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userAuthenticationService.authenticate(new AuthenticateUserServiceRequest(loginRequest.email(), loginRequest.password()))
                .fold(
                        ResponseMapper::mapError,
                        jwt -> ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(jwt))
                );
    }
}
