package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.OrderRequest;
import org.example.ecommers.dto.response.OrderResponse;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.Order;

public interface OrderMapper {

    Order toEntityFromCart(Cart cart);
    OrderResponse toDto(Order order);
}
