package org.example.ecommers.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
