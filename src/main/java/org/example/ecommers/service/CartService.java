package org.example.ecommers.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.ecommers.dto.CartDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.User;
import org.example.ecommers.exception.cart.CartAlreadyExistsException;
import org.example.ecommers.exception.cart.CartNotFoundException;
import org.example.ecommers.mapper.CartItemMapperImpl;
import org.example.ecommers.mapper.CartMapper;
import org.example.ecommers.mapper.CartMapperImpl;
import org.example.ecommers.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapperImpl cartMapperImpl;

    public CartDto createCartForUser(User user) {

        if (cartRepository.existsById(user.getId())) {
            throw new CartAlreadyExistsException(user.getId());
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);

        Cart cart1 = cartRepository.save(cart);
        return cartMapperImpl.toDto(cart1);
    }

    public CartDto getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return cartMapperImpl.toDto(cart);
    }

    public void cleanCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        cart.setCartItems(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public List<CartDto> getAllCart(){
        List<CartDto> carts = cartRepository.findAll()
                .stream().map(cartMapperImpl::toDto).toList();
        return carts;

    }

    public void calculateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems()
                .stream()
                .map(item ->
                        item.getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
    }



}
