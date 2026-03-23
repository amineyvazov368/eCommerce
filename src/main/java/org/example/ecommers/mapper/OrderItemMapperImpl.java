package org.example.ecommers.mapper;

import org.example.ecommers.dto.response.OrderItemResponse;
import org.example.ecommers.entity.OrderItem;

public class OrderItemMapperImpl implements OrderItemMapper {
    @Override
    public OrderItemResponse toDto(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}
