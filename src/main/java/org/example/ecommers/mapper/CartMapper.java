package org.example.ecommers.mapper;

import org.example.ecommers.dto.CartDto;
import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;

public interface CartMapper {

    CartDto toDto(Cart cart);

    Cart toEntity(CartDto cartDto);




}
