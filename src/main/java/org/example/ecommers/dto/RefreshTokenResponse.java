package org.example.ecommers.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
