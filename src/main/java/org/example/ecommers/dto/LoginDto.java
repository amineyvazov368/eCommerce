package org.example.ecommers.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "Username cannot be blank")
        String userName,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
