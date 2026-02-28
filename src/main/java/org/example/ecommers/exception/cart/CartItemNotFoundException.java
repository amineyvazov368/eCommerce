package org.example.ecommers.exception.cart;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String message) {
        super("CartItem not found: " + message);
    }
}
