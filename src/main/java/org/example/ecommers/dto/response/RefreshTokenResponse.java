package org.example.ecommers.dto.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
