package org.example.ecommers.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.OrderRequest;
import org.example.ecommers.dto.response.OrderResponse;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.Order;
import org.example.ecommers.entity.OrderStatus;
import org.example.ecommers.exception.cart.CartNotFoundException;
import org.example.ecommers.mapper.OrderMapper;
import org.example.ecommers.mapper.OrderMapperImpl;
import org.example.ecommers.repository.CartRepository;
import org.example.ecommers.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapperImpl orderMapper;
    private final CartRepository cartRepository;


    public OrderResponse createOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = orderMapper.toEntityFromCart(cart);
        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndStatusNot(userId, OrderStatus.CANCELLED);
        return orders.stream().map(orderMapper::toDto)
                .toList();
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = orderRepository.findByStatusNot(OrderStatus.CANCELLED);
        return orders.stream().map(orderMapper::toDto)
                .toList();
    }

    public OrderResponse cancelOrder(Long orderId, Long userId, boolean isAdmin){
        Order order= orderRepository.findById(Math.toIntExact(orderId))
                .orElseThrow(()->new RuntimeException("order not found"));

        if (!isAdmin && !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("This is not your order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }
}
