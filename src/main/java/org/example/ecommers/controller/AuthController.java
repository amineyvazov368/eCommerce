package org.example.ecommers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.LoginRequest;
import org.example.ecommers.dto.request.RefreshTokenRequest;
import org.example.ecommers.dto.request.UserRequest;
import org.example.ecommers.dto.response.AuthResponse;
import org.example.ecommers.dto.response.RefreshTokenResponse;
import org.example.ecommers.dto.response.UserResponse;
import org.example.ecommers.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class AuthController {


    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userDto) {
        UserResponse savedUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid LoginRequest loginDto) {
        AuthResponse loginUser = userService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

    @CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.refresh(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        userService.logout(authHeader);
        return ResponseEntity.ok("Uğurla çıxış edildi.");
    }

}
