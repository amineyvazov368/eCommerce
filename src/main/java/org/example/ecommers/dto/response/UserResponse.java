package org.example.ecommers.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.ecommers.entity.Role;

public record UserResponse(

        @NotNull
        Long id,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String userName,

        @NotBlank
        @Email
        String email,

        boolean isActive,

        @NotNull
        Role role
) {
}
