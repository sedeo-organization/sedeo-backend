package com.sedeo.domain.friend.configuration;

import com.sedeo.domain.friend.db.DetailedFriendshipInvitationRepository;
import com.sedeo.domain.friend.db.FriendRepository;
import com.sedeo.domain.friend.db.FriendshipInvitationRepository;
import com.sedeo.domain.friend.db.FriendshipRepository;
import com.sedeo.domain.friend.facade.*;
import com.sedeo.domain.friend.listener.FriendEventListener;
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
