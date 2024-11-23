package com.sedeo.authentication.model;

public record AuthenticateUserServiceRequest(String email, String plainPassword) {
}
