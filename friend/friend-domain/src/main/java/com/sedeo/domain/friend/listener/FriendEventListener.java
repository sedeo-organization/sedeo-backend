package com.sedeo.domain.friend.listener;

import com.sedeo.domain.friend.facade.Friends;
import com.sedeo.domain.friend.model.Friend;
import com.sedeo.event.UserCreatedSuccessfullyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FriendEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendEventListener.class);

    private final Friends friends;

    public FriendEventListener(Friends friends) {
        this.friends = friends;
    }

    @EventListener
    public void handleUserCreatedSuccessfullyEvent(UserCreatedSuccessfullyEvent userCreatedSuccessfullyEvent) {
        UserCreatedSuccessfullyEvent.CreatedUserModel createdUser = userCreatedSuccessfullyEvent.getCreatedUserModel();
        Friend friend = new Friend(createdUser.userId(), createdUser.firstName(), createdUser.lastName(), createdUser.phoneNumber());
        friends.createFriend(friend)
                .peekLeft(error -> LOGGER.warn("Creation of a friend failed"))
                .peek(user -> LOGGER.info("Friend created successfully"));
    }

}
