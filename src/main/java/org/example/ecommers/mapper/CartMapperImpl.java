package org.example.ecommers.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.CartDto;
import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapperImpl implements CartMapper {

    private final CartItemMapperImpl cartItemMapper;

    @Override
    public   CartDto toDto(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCartItems().stream()
                        .map(CartItemMapperImpl::cartItemToDto)
                        .toList(),
                cart.getTotalPrice()
        );
    }

    @Override
    public Cart toEntity(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.id());

        User user = new User();
        user.setId(cartDto.userId());
        cart.setUser(user);

     List<CartItem> items = cartDto.cartItemList().stream()
                      .map(CartItemMapperImpl::cartItemDtoToEntity)
                              .toList();
     items.forEach(item -> cart.getCartItems().add(item));
     cart.setCartItems(items);
        cart.setTotalPrice(cartDto.totalPrice());
        return cart;
    }



}
