package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CartDto;
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
    public ResponseEntity<List<CartDto>> getCarts() {
        List<CartDto> cartDto = cartService.getAllCart();
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        CartDto cartDto = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartDto);
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long userId) {
        cartService.cleanCart(userId);
        return ResponseEntity.noContent().build();
    }


}
// eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMyIsInVzZXJuYW1lIjoiYWRtaW4iLCJpYXQiOjE3NzMzOTk2MzMsImV4cCI6MTc3MzQwMDUzM30.ochi2AYqMiCt8_RSEcUmwRkJVccGmrAsflBh03m8kFvfcg7DpLzVjDCryShgkoUk

// eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMyIsInVzZXJuYW1lIjoiYWRtaW4iLCJpYXQiOjE3NzMzOTk2MzMsImV4cCI6MTc3MzQwMDUzM30.ochi2AYqMiCt8_RSEcUmwRkJVccGmrAsflBh03m8kFvfcg7DpLzVjDCryShgkoUk