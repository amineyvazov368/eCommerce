package org.example.ecommers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.AuthResponse;
import org.example.ecommers.dto.LoginDto;
import org.example.ecommers.dto.UserDto;
import org.example.ecommers.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserDto userDto) {
        UserDto savedUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid LoginDto loginDto) {
        AuthResponse loginUser = userService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

}
