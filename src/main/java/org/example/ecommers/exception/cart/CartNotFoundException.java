package org.example.ecommers.exception.cart;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long id) {
        super("Cart not found: " + id);
    }
}
