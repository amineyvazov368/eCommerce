package org.example.ecommers.dto.response;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        Long id,

        @NotBlank(message = "Category name cannot be blank")
        String name

) {
}
