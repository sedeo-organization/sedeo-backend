package com.sedeo.controllers.dto;

import com.sedeo.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserControllerMapper {

    UserControllerMapper INSTANCE = Mappers.getMapper(UserControllerMapper.class);

    FetchUserProfileResponse userToFetchUserProfileResponse(User user);

    FetchFriendsResponse.Friend userToFetchFriendResponseFriend(User user);

    List<FetchFriendsResponse.Friend> usersToFetchFriendsResponseFriends(List<User> users);

    default FetchFriendsResponse usersToFetchFriendsResponse(List<User> users) {
        List<FetchFriendsResponse.Friend> friends = usersToFetchFriendsResponseFriends(users);
        return new FetchFriendsResponse(friends);
    }

    FetchFriendInvitationsResponse.InvitingUser userToFetchFriendInvitationsResponseInvitingUser(User user);

    List<FetchFriendInvitationsResponse.InvitingUser> usersToFetchFriendInvitationsResponseInvitingUser(List<User> users);

    default FetchFriendInvitationsResponse usersToFetchFriendInvitationsResponse(List<User> users) {
        List<FetchFriendInvitationsResponse.InvitingUser> invitingUsers = usersToFetchFriendInvitationsResponseInvitingUser(users);
        return new FetchFriendInvitationsResponse(invitingUsers);
    }

}
