package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CartDto;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.user.UserNotFoundException;
import org.example.ecommers.repository.UserRepository;
import org.example.ecommers.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository UserRepository;

    @GetMapping("/me")
    public ResponseEntity<CartDto> getMyCart(Authentication authentication) {
        String username = authentication.getName();
        User user = UserRepository.findByUserName(username)
                .orElseThrow(()->new RuntimeException("User not found"));

        CartDto cartDto =cartService.getUserCart(user.getId());
        return ResponseEntity.ok(cartDto);


    }

}
