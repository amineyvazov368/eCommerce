package org.example.ecommers.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(

        @NotNull(message = "Cart id cannot be null")
        Long productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
