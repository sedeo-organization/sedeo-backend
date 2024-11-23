package com.sedeo.authentication.model;

public record RegisterUserServiceRequest(String email, String firstName, String lastName, String phoneNumber, String plainPassword) {
}
