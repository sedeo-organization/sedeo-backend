package com.sedeo.authentication.controller.dto;

public record RegisterRequest(String email, String firstName, String lastName, String phoneNumber, String password) {
}