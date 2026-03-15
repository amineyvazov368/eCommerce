package org.example.ecommers.mapper;

import org.example.ecommers.dto.request.CartItemRequest;
import org.example.ecommers.dto.response.CartItemResponse;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapperImpl {


    public static CartItemResponse cartItemToDto(CartItem item) {
        BigDecimal subTotal =
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice(),
                subTotal,
                item.getCreatedAt()
        );
    }


    public static CartItem cartItemDtoToEntity(CartItemRequest dto) {
        CartItem item = new CartItem();

        Cart cart = new Cart();
       cart.setId(dto.cartId());

        Product product = new Product();
        product.setId(dto.productId());

        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(dto.quantity());

        return item;
    }
}
