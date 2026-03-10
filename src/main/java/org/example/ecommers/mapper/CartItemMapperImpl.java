package org.example.ecommers.mapper;

import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapperImpl {


    public static CartItemDto cartItemToDto(CartItem item) {
        return new CartItemDto(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice()
        );
    }


    public static CartItem cartItemDtoToEntity(CartItemDto dto) {
        CartItem item = new CartItem();
        item.setId(dto.id());
        Product product = new Product();
        product.setId(dto.productId());
        item.setProduct(product);
        item.setQuantity(dto.quantity());
        item.setPrice(dto.price());
        return item;
    }
}
