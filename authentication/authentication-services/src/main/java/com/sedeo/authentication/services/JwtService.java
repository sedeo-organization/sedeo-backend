package com.sedeo.authentication.services;

import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;

public interface JwtService {

    Either<GeneralError, String> generate(String subject);
}
