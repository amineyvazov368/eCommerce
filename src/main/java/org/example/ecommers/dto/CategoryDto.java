package org.example.ecommers.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        Long id,

        @NotBlank(message = "Category name cannot be blank")
        String name

) {
}
