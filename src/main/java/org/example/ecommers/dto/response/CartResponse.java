package org.example.ecommers.dto.response;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(

        @NotNull
        Long id,

        @NotNull
        Long userId,

        List<CartItemResponse> cartItems,

        @NotNull
        @DecimalMin(value = "0.0", message = "Total price must be greater than 0")
        BigDecimal totalPrice,

        @NotNull
        LocalDateTime createdAt
) {
}
