package org.example.ecommers.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.UserDto;
import org.example.ecommers.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

   private final PasswordEncoder passwordEncoder ;

    @Override
    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUserName(userDto.userName());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
//        user.setActive(userDto.isActive());
//        user.setRole(userDto.role());
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
                user.getPassword()
//                user.isActive(),
//                user.getRole()

        );
    }
}
