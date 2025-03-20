package com.sedeo.controllers.dto;

import com.sedeo.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserControllerMapper {

    UserControllerMapper INSTANCE = Mappers.getMapper(UserControllerMapper.class);

    FetchUserProfileResponse userToFetchUserProfileResponse(User user);
}
