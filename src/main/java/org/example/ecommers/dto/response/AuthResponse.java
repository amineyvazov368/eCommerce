package org.example.ecommers.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse userDto
) {
}
