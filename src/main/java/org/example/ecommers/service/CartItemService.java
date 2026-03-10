package org.example.ecommers.service;


import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.Product;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.cart.CartItemNotFoundException;
import org.example.ecommers.exception.cart.CartNotFoundException;
import org.example.ecommers.exception.product.ProductNotFoundException;
import org.example.ecommers.exception.user.UserNotFoundException;
import org.example.ecommers.mapper.CartItemMapperImpl;
import org.example.ecommers.mapper.CartMapperImpl;
import org.example.ecommers.repository.CartItemRepository;
import org.example.ecommers.repository.CartRepository;
import org.example.ecommers.repository.ProductRepository;
import org.example.ecommers.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapperImpl cartItemMapperImpl;

    CartItemDto addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(productId)
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
        }

        cartItemRepository.save(cartItem);

        return cartItemMapperImpl.cartItemToDto(cartItem);

    }

    public List<CartItemDto> findAllByCart(Cart cart) {
        Optional<CartItem> find = cartItemRepository.findAllByCart(cart);

        return find.stream().map(CartItemMapperImpl::cartItemToDto)
                .toList();

    }

    public CartItemDto increaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId.toString()));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);
        return cartItemMapperImpl.cartItemToDto(cartItem);
    }

    public CartItemDto decreaseQuantity(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId.toString()));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
        return cartItemMapperImpl.cartItemToDto(cartItem);
    }

    public void removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItemRepository.delete(cartItem);
    }


}
