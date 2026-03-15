package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.CartRequest;
import org.example.ecommers.dto.response.CartResponse;
import org.example.ecommers.entity.Cart;

public interface CartMapper {

    CartResponse toDto(Cart cart);

    Cart toEntity(CartRequest cartDto);


}
