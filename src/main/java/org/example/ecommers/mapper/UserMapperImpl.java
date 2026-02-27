package org.example.ecommers.mapper;

import org.example.ecommers.dto.UserDto;
import org.example.ecommers.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUserName(userDto.userName());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRole(userDto.role());
        return user;

    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()

        );
    }
}
