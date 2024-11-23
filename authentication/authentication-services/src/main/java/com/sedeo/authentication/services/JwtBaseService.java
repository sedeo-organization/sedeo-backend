package com.sedeo.authentication.services;

import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

public class JwtBaseService implements JwtService {

    private static final String ALGORITHM = "HS256";

    private final JwtEncoder jwtEncoder;
    private final JwtConfiguration jwtConfiguration;

    public JwtBaseService(JwtEncoder jwtEncoder, JwtConfiguration jwtConfiguration) {
        this.jwtEncoder = jwtEncoder;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public Either<GeneralError, String> generate(String subject) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(now.plusSeconds(jwtConfiguration.durationInSeconds()))
                .subject(subject)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> ALGORITHM).build();
        String jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        return Either.right(jwtToken);
    }
}
