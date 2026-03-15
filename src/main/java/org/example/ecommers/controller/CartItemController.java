package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.CartItemResponse;
import org.example.ecommers.dto.response.CartResponse;
import org.example.ecommers.mapper.CartMapper;
import org.example.ecommers.service.CartItemService;
import org.example.ecommers.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addProductToCart(@RequestParam Long userId,
                                                             @RequestParam Long productId,
                                                             @RequestParam int quantity) {

        CartItemResponse cartItemDto= cartItemService.addProductToCart(userId,productId,quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable Long userId) {

        CartResponse cartResponse = cartService.getUserCart(userId);
        List<CartItemResponse> items = cartItemService.findAllByCartId(cartResponse.id());
        return ResponseEntity.ok(items);
    }

    @PutMapping("/increase/{cartItemId}")
    public ResponseEntity<CartItemResponse> increaseQuantity(@PathVariable Long cartItemId){

        CartItemResponse cartItemDto=cartItemService.increaseQuantity(cartItemId);
        return ResponseEntity.ok(cartItemDto);

    }

    @PutMapping("/decrease/{cartItemId}")
    public ResponseEntity<CartItemResponse> decreaseQuantity(@PathVariable Long cartItemId){
        CartItemResponse cartItemDto=cartItemService.decreaseQuantity(cartItemId);
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId){
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteAllCartItem(@PathVariable Long cartId){
        cartItemService.removeAllCartItems(cartId);
        return ResponseEntity.noContent().build();
    }


}
