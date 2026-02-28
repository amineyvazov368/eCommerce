package org.example.ecommers.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.ecommers.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        Long id,

        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotNull
        List<CartItemDto> cartItemList,

        @NotNull
        @DecimalMin(value = "0.0", message = "Total price must be greater than 0")
        BigDecimal totalPrice
) {
}
