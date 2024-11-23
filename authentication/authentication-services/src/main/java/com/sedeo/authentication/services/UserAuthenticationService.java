package com.sedeo.authentication.services;

import com.sedeo.authentication.model.AuthenticateUserServiceRequest;
import com.sedeo.authentication.model.RegisterUserServiceRequest;
import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;

public interface UserAuthenticationService {

    Either<GeneralError, String> authenticate(AuthenticateUserServiceRequest authenticateUserServiceRequest);

    Either<GeneralError, Void> register(RegisterUserServiceRequest registerUserServiceRequest);
}
