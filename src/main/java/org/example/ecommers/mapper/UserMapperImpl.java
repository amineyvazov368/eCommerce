package org.example.ecommers.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.UserRequest;
import org.example.ecommers.dto.response.UserResponse;
import org.example.ecommers.entity.Role;
import org.example.ecommers.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

   private final PasswordEncoder passwordEncoder ;

    @Override
    public User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setId(userRequest.id());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUserName(userRequest.userName());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        return user;

    }

    @Override
    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getEmail(),
                user.isActive(),
                user.getRole()

        );
    }
}
