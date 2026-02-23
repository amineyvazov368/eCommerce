package org.example.ecommers.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDto(
        Long id,

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @Min(value = 0, message = "Stock must be zero or greater")
        int stock,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotBlank(message = "Image URL cannot be blank")
        String image,

        @NotNull(message = "Category ID cannot be null")
        Long CategoryId
) {
}
