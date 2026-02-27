package org.example.ecommers.mapper;

import org.example.ecommers.dto.UserDto;
import org.example.ecommers.entity.User;

public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
