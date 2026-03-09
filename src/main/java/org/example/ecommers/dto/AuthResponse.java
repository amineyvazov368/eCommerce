package org.example.ecommers.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserDto userDto
) {
}
