package com.sedeo.user.model;

import java.math.BigDecimal;
import java.util.UUID;

public record User(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        BigDecimal accountBalance
) {
}
