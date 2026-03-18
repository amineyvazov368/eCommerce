package org.example.ecommers.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.LoginRequest;
import org.example.ecommers.dto.request.RefreshTokenRequest;
import org.example.ecommers.dto.request.UserRequest;
import org.example.ecommers.dto.response.AuthResponse;
import org.example.ecommers.dto.response.RefreshTokenResponse;
import org.example.ecommers.dto.response.UserResponse;
import org.example.ecommers.entity.Role;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.user.UserAlreadyExistsException;
import org.example.ecommers.exception.user.UserNotFoundException;
import org.example.ecommers.mapper.UserMapper;
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

    public UserResponse registerUser(UserRequest userRequest) {

        if (userRepository.existsByUserName(userRequest.userName())) {
            throw new UserAlreadyExistsException(userRequest.userName());
        }
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistsException(userRequest.email());
        }


        User user = userMapper.toEntity(userRequest);
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        User userSave = userRepository.save(user);
        cartService.createCartForUser(userSave);

        return userMapper.toDto(userSave);

    }

    public AuthResponse login(LoginRequest loginDto) {

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

        UserResponse userDto = userMapper.toDto(user);
        return new AuthResponse(accessToken, refreshToken, userDto);
    }

    private final java.util.Set<String> tokenBlacklist = java.util.Collections.synchronizedSet(new java.util.HashSet<>());

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isValid(refreshToken)) {
            throw new RuntimeException("Refresh token is invalid");
        }
        Long userId = jwtService.extractUserId(refreshToken);
        String userName = jwtService.extractUsername(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(userId, userName);
        return new RefreshTokenResponse(newAccessToken, refreshToken);

    }


    public UserResponse getUserById(long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllByIsActiveTrue()
                .stream().map(userMapper::toDto)
                .toList();
    }

    public void deleteUserById(long id) {
        User user =userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        userRepository.save(user);
    }

    public UserResponse updateUser(long id, UserRequest userDto) {
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

    public UserResponse updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);

        return userMapper.toDto(user);
    }


}
