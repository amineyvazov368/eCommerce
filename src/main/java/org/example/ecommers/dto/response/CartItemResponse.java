package org.example.ecommers.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemResponse(


        @NotNull
        Long id,

        @NotNull
        Long productId,

        String productName,

        @Min(1)
        int quantity,

        @NotNull
        BigDecimal price,

        @NotNull
        BigDecimal subTotal,

        @NotNull
        LocalDateTime createdAt

) {
}
