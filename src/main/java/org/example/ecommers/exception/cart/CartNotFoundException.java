package org.example.ecommers.exception.cart;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String message) {
        super("Cart not found: " + message);
    }
}
