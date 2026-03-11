package org.example.ecommers.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CartDto;
import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.mapper.CartMapper;
import org.example.ecommers.mapper.CartMapperImpl;
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
    public ResponseEntity<CartItemDto> addProductToCart(@RequestParam Long userId,
                                                        @RequestParam Long productId,
                                                        @RequestParam int quantity) {

       CartItemDto cartItemDto= cartItemService.addProductToCart(userId,productId,quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("cart/{userId}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long userId) {

        CartDto cartDto=cartService.getUserCart(userId);
        Cart cart = cartMapper.toEntity(cartDto);
       return ResponseEntity.ok(cartItemService.findAllByCart(cart));
    }

    @PutMapping("/increase/{cartItemId}")
    public ResponseEntity<CartItemDto> increaseQuantity(@PathVariable Long cartItemId){

        CartItemDto cartItemDto=cartItemService.increaseQuantity(cartItemId);
        return ResponseEntity.ok(cartItemDto);

    }

    @PutMapping("/decrease/{cartItemId}")
    public ResponseEntity<CartItemDto> decreaseQuantity(@PathVariable Long cartItemId){
        CartItemDto cartItemDto=cartItemService.decreaseQuantity(cartItemId);
        return ResponseEntity.ok(cartItemDto);
    }

    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId){
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

}
