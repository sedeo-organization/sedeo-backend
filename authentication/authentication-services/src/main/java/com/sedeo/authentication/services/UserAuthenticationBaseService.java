package com.sedeo.authentication.services;

import com.sedeo.authentication.model.AuthenticateUserServiceRequest;
import com.sedeo.authentication.model.RegisterUserServiceRequest;
import com.sedeo.authentication.model.error.UserAuthenticationError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.user.facade.Users;
import com.sedeo.domain.user.model.User;
import io.vavr.control.Either;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserAuthenticationBaseService implements UserAuthenticationService {

    private final Users users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserAuthenticationBaseService(Users users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Either<GeneralError, String> authenticate(AuthenticateUserServiceRequest authenticateUserServiceRequest) {
        return users.fetchUser(authenticateUserServiceRequest.email())
                .filterOrElse(user -> passwordEncoder.matches(authenticateUserServiceRequest.plainPassword(), user.password()), error -> new UserAuthenticationError.EmailOrPasswordIncorrectError())
                .flatMap(user -> jwtService.generate(user.userId().toString()));
    }

    @Override
    public Either<GeneralError, Void> register(RegisterUserServiceRequest registerUserServiceRequest) {
        if (users.userExists(registerUserServiceRequest.email(), registerUserServiceRequest.phoneNumber())) {
            return Either.left(new UserAuthenticationError.UserAlreadyExistsError());
        }

        User registeredUser = User.withZeroBalance(UUID.randomUUID(),
                registerUserServiceRequest.firstName(),
                registerUserServiceRequest.lastName(),
                registerUserServiceRequest.phoneNumber(),
                registerUserServiceRequest.email(),
                passwordEncoder.encode(registerUserServiceRequest.plainPassword()));

        return users.createUser(registeredUser);
    }
}
