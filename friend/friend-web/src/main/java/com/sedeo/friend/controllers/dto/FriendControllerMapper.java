package com.sedeo.friend.controllers.dto;

import com.sedeo.friend.model.DetailedFriendshipInvitation;
import com.sedeo.friend.model.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FriendControllerMapper {

    FriendControllerMapper INSTANCE = Mappers.getMapper(FriendControllerMapper.class);

    FetchFriendsResponse.Friend friendToFetchFriendResponseFriend(Friend friend);

    List<FetchFriendsResponse.Friend> friendsToFetchFriendsResponseFriends(List<Friend> friends);

    default FetchFriendsResponse friendsToFetchFriendsResponse(List<Friend> friends) {
        List<FetchFriendsResponse.Friend> fetchFriendsResponseFriends = friendsToFetchFriendsResponseFriends(friends);
        return new FetchFriendsResponse(fetchFriendsResponseFriends);
    }

    FetchPotentialFriendsResponse.PotentialFriend friendToFetchPotentialFriendsResponsePotentialFriend(Friend friend);

    List<FetchPotentialFriendsResponse.PotentialFriend> friendsToFetchPotentialFriendsResponsePotentialFriends(List<Friend> friends);

    default FetchPotentialFriendsResponse friendsToFetchPotentialFriendsResponse(List<Friend> friends) {
        List<FetchPotentialFriendsResponse.PotentialFriend> potentialFriends = friendsToFetchPotentialFriendsResponsePotentialFriends(friends);
        return new FetchPotentialFriendsResponse(potentialFriends);
    }

    @Mapping(source = "invitingUser.userId", target = "userId")
    @Mapping(source = "invitingUser.firstName", target = "firstName")
    @Mapping(source = "invitingUser.lastName", target = "lastName")
    @Mapping(source = "invitingUser.phoneNumber", target = "phoneNumber")
    FetchFriendshipInvitationsResponse.Invitation detailedFriendshipInvitationToFetchFriendshipInvitationResponseInvitation(DetailedFriendshipInvitation invitation);

    List<FetchFriendshipInvitationsResponse.Invitation> detailedFriendshipInvitationsToFetchFriendshipInvitationResponseInvitations(List<DetailedFriendshipInvitation> invitations);

    default FetchFriendshipInvitationsResponse detailedFriendshipInvitationsToFetchFriendshipInvitationResponse(List<DetailedFriendshipInvitation> invitations) {
        return new FetchFriendshipInvitationsResponse(detailedFriendshipInvitationsToFetchFriendshipInvitationResponseInvitations(invitations));
    }
}
