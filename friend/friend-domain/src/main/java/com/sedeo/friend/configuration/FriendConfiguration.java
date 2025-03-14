package com.sedeo.friend.configuration;

import com.sedeo.friend.db.DetailedFriendshipInvitationRepository;
import com.sedeo.friend.db.FriendRepository;
import com.sedeo.friend.db.FriendshipInvitationRepository;
import com.sedeo.friend.db.FriendshipRepository;
import com.sedeo.friend.facade.*;
import com.sedeo.friend.listener.FriendEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FriendConfiguration {

    @Bean
    Friends friends(FriendRepository friendRepository) {
        return new FriendsFacade(friendRepository);
    }

    @Bean
    Friendships friendships(FriendshipRepository friendshipRepository) {
        return new FriendshipsFacade(friendshipRepository);
    }

    @Bean
    FriendshipInvitations friendshipInvitations(FriendshipInvitationRepository friendshipInvitationRepository,
                                                FriendshipRepository friendshipRepository, Friendships friendships) {
        return new FriendshipInvitationsFacade(friendshipInvitationRepository, friendshipRepository, friendships);
    }

    @Bean
    DetailedFriendshipInvitations detailedFriendshipInvitations(DetailedFriendshipInvitationRepository detailedFriendshipInvitationRepository) {
        return new DetailedFriendshipInvitationsFacade(detailedFriendshipInvitationRepository);
    }

    @Bean
    FriendEventListener friendEventListener(Friends friends) {
        return new FriendEventListener(friends);
    }

}
