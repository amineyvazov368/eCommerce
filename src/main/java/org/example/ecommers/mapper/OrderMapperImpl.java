package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.OrderRequest;
import org.example.ecommers.dto.response.OrderItemResponse;
import org.example.ecommers.dto.response.OrderResponse;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.Order;
import org.example.ecommers.entity.OrderItem;
import org.example.ecommers.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntityFromCart(Cart cart) {
     Order order = new Order();
     order.setUser(cart.getUser());
     order.setStatus(OrderStatus.PENDING);

    List<OrderItem> items=  cart.getCartItems().stream()
             .map(item -> {
                 OrderItem orderItem = new OrderItem();
                 orderItem.setProduct(item.getProduct());
                 orderItem.setProductName(item.getProduct().getName());
                 orderItem.setQuantity(item.getQuantity());
                 orderItem.setPrice(item.getPrice());
                 orderItem.setOrder(order);
                 return orderItem;
             }).collect(Collectors.toList());
        BigDecimal totalPrice = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    order.setItems(items);
    order.setTotalPrice(totalPrice);

    return order;
    }

    @Override
    public OrderResponse toDto(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProductName(),
                        i.getQuantity(),
                        i.getPrice()
                )).collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCreateAt(),
                order.getTotalPrice(),
                order.getStatus(),
                itemResponses
        );
    }

}

