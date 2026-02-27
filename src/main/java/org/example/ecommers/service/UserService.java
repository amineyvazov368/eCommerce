package org.example.ecommers.service;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.LoginDto;
import org.example.ecommers.dto.UserDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.User;
import org.example.ecommers.mapper.UserMapper;
import org.example.ecommers.mapper.UserMapperImpl;
import org.example.ecommers.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;

    UserDto registerUser(UserDto userDto) {

        if (userRepository.existsByUserName(userDto.userName())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(userDto);
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        User userSave = userRepository.save(user);

        return userMapper.toDto(userSave);

    }

    public UserDto login(LoginDto loginDto) {

        User user = userRepository.findByUserName(loginDto.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(loginDto.password())) {
            throw new RuntimeException("Wrong password");
        }
        return userMapper.toDto(user);
    }

    public UserDto getUserById(long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(userMapper::toDto)
                .toList();
    }

    public void deleteUserById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }

    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!userDto.userName().equals(user.getUserName())
                && userRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("Username already exists");
        }
        if (!userDto.email().equals(user.getEmail())
                && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
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
