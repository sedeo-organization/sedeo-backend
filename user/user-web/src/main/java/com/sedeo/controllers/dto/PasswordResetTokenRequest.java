package com.sedeo.controllers.dto;

import jakarta.validation.constraints.Email;

public record PasswordResetTokenRequest(
        @Email String email
) {
}
