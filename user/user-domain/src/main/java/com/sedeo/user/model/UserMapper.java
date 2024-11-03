package com.sedeo.user.model;

import com.sedeo.user.db.model.FriendInvitationEntity;
import com.sedeo.user.db.model.FriendshipEntity;
import com.sedeo.user.db.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity userToUserEntity(User user);

    User userEntityToUser(UserEntity userEntity);

    List<User> userEntityListToUser(List<UserEntity> userEntities);

    FriendshipEntity friendshipToFriendshipEntity(Friendship friendship);

    Friendship friendshipEntityToFriendship(FriendshipEntity friendshipEntity);

    List<Friendship> friendshipEntityListToFriendshipList(List<FriendshipEntity> friendshipEntityList);

    FriendInvitation friendInvitationEntityToFriendInvitation(FriendInvitationEntity friendInvitationEntity);

    FriendInvitationEntity friendInvitationToFriendInvitationEntity(FriendInvitation friendInvitation);
}
