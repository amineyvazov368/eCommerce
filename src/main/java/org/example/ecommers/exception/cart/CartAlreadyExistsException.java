package org.example.ecommers.exception.cart;

public class CartAlreadyExistsException extends RuntimeException{

    public CartAlreadyExistsException(Long id) {
        super(id + " already exists");
    }
}
