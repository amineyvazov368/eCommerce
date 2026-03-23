package org.example.ecommers.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.response.CartItemResponse;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.Product;
import org.example.ecommers.exception.cart.CartItemNotFoundException;
import org.example.ecommers.exception.cart.CartNotFoundException;
import org.example.ecommers.exception.product.ProductNotFoundException;
import org.example.ecommers.mapper.CartItemMapperImpl;
import org.example.ecommers.repository.CartItemRepository;
import org.example.ecommers.repository.CartRepository;
import org.example.ecommers.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final CartItemMapperImpl cartItemMapperImpl;

    public CartItemResponse addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;

        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
        }

        cartItemRepository.save(cartItem);
        cartService.calculateCartTotal(cart);
        cartRepository.save(cart);

        return cartItemMapperImpl.cartItemToDto(cartItem);

    }

    public List<CartItemResponse> findAllByCartId(Long cartId){
        return cartItemRepository.findByCartId(cartId)
                .stream()
                .map(CartItemMapperImpl::cartItemToDto)
                .toList();
    }

    public List<CartItemResponse> findAllByCart(Cart cart) {
        List<CartItem> find = cartItemRepository.findAllByCart(cart);

        return find.stream().map(CartItemMapperImpl::cartItemToDto)
                .toList();

    }

    public CartItemResponse increaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId.toString()));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
        Cart cart = cartItem.getCart();
        cartService.calculateCartTotal(cart);
        cartRepository.save(cart);

        return cartItemMapperImpl.cartItemToDto(cartItem);
    }

    public CartItemResponse decreaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId.toString()));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
            Cart cart = cartItem.getCart();
            cartService.calculateCartTotal(cart);
            cartRepository.save(cart);
        } else {
            cartItemRepository.delete(cartItem);
        }
        return cartItemMapperImpl.cartItemToDto(cartItem);
    }

    public void removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id " + cartItemId));

        Cart cart = cartItem.getCart();
        cartItemRepository.delete(cartItem);
        if (cart != null) {
            cartService.calculateCartTotal(cart);
             cartRepository.save(cart);
        }
    }

    public void removeAllCartItems(Long cartId) {
        cartService.cleanCart(cartId);
    }


}
