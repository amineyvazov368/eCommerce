package org.example.ecommers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,

        @NotNull(message = "Product id cannot be null")
        Long productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotNull(message = "Price cannot be null")
        BigDecimal price
) {
}
