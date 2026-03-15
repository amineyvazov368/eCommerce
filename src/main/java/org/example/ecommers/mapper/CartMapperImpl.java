package org.example.ecommers.mapper;

import lombok.RequiredArgsConstructor;
import org.example.ecommers.dto.request.CartRequest;
import org.example.ecommers.dto.response.CartResponse;
import org.example.ecommers.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapperImpl implements CartMapper {

    private final CartItemMapperImpl cartItemMapper;

    @Override
    public CartResponse toDto(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCartItems().stream()
                        .map(CartItemMapperImpl::cartItemToDto)
                        .toList(),
                cart.getTotalPrice(),
                cart.getCreatedAt()
        );
    }

    @Override
    public Cart toEntity(CartRequest cartDto) {
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

//        return Cart.builder()
//                .id(cartDto.id())
//                .user(user)
//                .cartItems(items)
//                .totalPrice(cartDto.totalPrice())
//                .build();
    }

}
