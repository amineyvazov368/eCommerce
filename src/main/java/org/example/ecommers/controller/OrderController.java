package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.OrderResponse;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.user.UserNotFoundException;
import org.example.ecommers.repository.UserRepository;
import org.example.ecommers.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/cart")
    public ResponseEntity<OrderResponse> createOrder(Authentication auth) {

        String username = auth.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderResponse orderResponse = orderService.createOrder(user.getId());

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getUserOrders(Authentication auth){
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = auth.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<OrderResponse> orderResponse = orderService.getUserOrders(user.getId());
        return ResponseEntity.ok(orderResponse);
    }


    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, Authentication auth){
        String username = auth.getName();
        User user =userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        orderService.cancelOrder(orderId,user.getId(),false);

        return ResponseEntity.noContent().build();
    }
}
