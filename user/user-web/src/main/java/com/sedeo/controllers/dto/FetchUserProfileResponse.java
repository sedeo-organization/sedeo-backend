package com.sedeo.controllers.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FetchUserProfileResponse(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        BigDecimal accountBalance
) {
}
