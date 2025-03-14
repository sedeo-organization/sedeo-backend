package com.sedeo.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class UserCreatedSuccessfullyEvent extends ApplicationEvent {

    private final UserCreatedSuccessfullyEvent.CreatedUserModel createdUserModel;

    public UserCreatedSuccessfullyEvent(Object source, UserCreatedSuccessfullyEvent.CreatedUserModel createdUserModel) {
        super(source);
        this.createdUserModel = createdUserModel;
    }

    public UserCreatedSuccessfullyEvent.CreatedUserModel getCreatedUserModel() {
        return createdUserModel;
    }

    public record CreatedUserModel(UUID userId, String firstName, String lastName, String phoneNumber) {
    }
}
