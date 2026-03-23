package org.example.ecommers.mapper;

import org.example.ecommers.dto.response.OrderItemResponse;
import org.example.ecommers.entity.OrderItem;

public interface OrderItemMapper {
    OrderItemResponse toDto(OrderItem orderItem);
}
