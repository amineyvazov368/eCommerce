package org.example.ecommers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.ecommers.entity.Role;

public record UserDto(
        Long id,

        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,

        @NotBlank(message = "UserName cannot be blank")
        String userName,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 3, max = 50)
        String password,

        @NotNull(message = "Role cannot be null")
        Role role
) {
}
