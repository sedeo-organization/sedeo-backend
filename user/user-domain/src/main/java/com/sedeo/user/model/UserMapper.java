package com.sedeo.user.model;

import com.sedeo.user.db.model.PasswordResetTokenEntity;
import com.sedeo.user.db.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity userToUserEntity(User user);

    User userEntityToUser(UserEntity userEntity);

    List<User> userEntitiesToUsers(List<UserEntity> userEntities);

    List<User> userEntityListToUser(List<UserEntity> userEntities);

    PasswordResetTokenEntity passwordResetTokenToPasswordResetTokenEntity(PasswordResetToken passwordResetToken);

    PasswordResetToken passwordResetTokenEntityToPasswordResetToken(PasswordResetTokenEntity passwordResetTokenEntity);
}
