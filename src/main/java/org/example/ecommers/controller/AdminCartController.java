package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.CartResponse;
import org.example.ecommers.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/carts")
@RequiredArgsConstructor
public class AdminCartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponse>> getCarts() {
        List<CartResponse> cartDto = cartService.getAllCart();
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        CartResponse cartDto = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long userId) {
        cartService.cleanCart(userId);
        return ResponseEntity.noContent().build();
    }


}
