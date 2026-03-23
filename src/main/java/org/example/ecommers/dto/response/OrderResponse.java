package org.example.ecommers.dto.response;

import org.example.ecommers.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime crateAt,
        BigDecimal totalPrice,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}
