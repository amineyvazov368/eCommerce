package org.example.ecommers.mapper;

import org.example.ecommers.dto.CartDto;
import org.example.ecommers.dto.CartItemDto;
import org.example.ecommers.entity.Cart;
import org.example.ecommers.entity.CartItem;
import org.example.ecommers.entity.Product;
import org.example.ecommers.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {


    @Override
    public CartDto toDto(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCartItems().stream()
                        .map(this::cartItemToDto)
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
                      .map(this::cartItemDtoToEntity)
                              .toList();
     items.forEach(item -> cart.getCartItems().add(item));
     cart.setCartItems(items);
        cart.setTotalPrice(cartDto.totalPrice());
        return cart;
    }



    private CartItemDto cartItemToDto(CartItem item) {
        return new CartItemDto(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice()
        );
    }

    // Helper: CartItemDto -> CartItem
    private CartItem cartItemDtoToEntity(CartItemDto dto) {
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
