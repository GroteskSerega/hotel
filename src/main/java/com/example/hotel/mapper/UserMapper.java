package com.example.hotel.mapper;

import com.example.hotel.entity.User;
import com.example.hotel.web.dto.v1.UserListResponse;
import com.example.hotel.web.dto.v1.UserResponse;
import com.example.hotel.web.dto.v1.UserUpsertRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UserUpsertRequest request);

    UserResponse userToResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        return new UserListResponse(users
                .stream()
                .map(this::userToResponse)
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    void updateUser(UserUpsertRequest request, @MappingTarget User user);
}
