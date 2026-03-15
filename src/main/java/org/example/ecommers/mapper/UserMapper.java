package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.UserRequest;
import org.example.ecommers.dto.response.UserResponse;
import org.example.ecommers.entity.User;

public interface UserMapper {

    User toEntity(UserRequest userRequest);

    UserResponse toDto(User user);
}
