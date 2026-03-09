package org.example.ecommers.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.AuthResponse;
import org.example.ecommers.dto.LoginDto;
import org.example.ecommers.dto.UserDto;
import org.example.ecommers.entity.Role;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.user.UserAlreadyExistsException;
import org.example.ecommers.exception.user.UserNotFoundException;
import org.example.ecommers.mapper.UserMapperImpl;
import org.example.ecommers.repository.UserRepository;
import org.example.ecommers.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;
    private final CartService cartService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(UserDto userDto) {

        if (userRepository.existsByUserName(userDto.userName())) {
            throw new UserAlreadyExistsException(userDto.userName());
        }
        if (userRepository.existsByEmail(userDto.email())) {
            throw new UserAlreadyExistsException(userDto.email());
        }


        User user = userMapper.toEntity(userDto);
        user.setActive(true);
        user.setRole(Role.USER);
        User userSave = userRepository.save(user);
        cartService.createCartForUser(userSave);

        return userMapper.toDto(userSave);

    }

    public AuthResponse login(LoginDto loginDto) {

        User user = userRepository.findByUserName(loginDto.userName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("Your account is deactivated.");
        }
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUserName());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getUserName());

        UserDto userDto = userMapper.toDto(user);
        return new AuthResponse(accessToken, refreshToken, userDto);
    }

    public UserDto getUserById(long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapper::toDto)
                .toList();
    }

    public void deleteUserById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }

    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (!userDto.userName().equals(user.getUserName())
                && userRepository.existsByUserName(user.getUserName())) {
            throw new UserAlreadyExistsException(user.getUserName());
        }
        if (!userDto.email().equals(user.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUserName(userDto.userName());
        user.setEmail(userDto.email());
        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(userDto.password());
        }
        return userMapper.toDto(userRepository.save(user));

    }


}
