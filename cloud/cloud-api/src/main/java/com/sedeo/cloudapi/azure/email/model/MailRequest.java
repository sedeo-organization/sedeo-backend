package com.sedeo.cloudapi.azure.email.model;

public record MailRequest(String recipient, String subject, String content) {
}
