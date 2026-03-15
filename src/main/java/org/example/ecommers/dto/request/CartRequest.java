package org.example.ecommers.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CartRequest(
        Long id,

        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotNull
        List<CartItemRequest> cartItemList,

        @NotNull
        @DecimalMin(value = "0.0", message = "Total price must be greater than 0")
        BigDecimal totalPrice
) {
}
