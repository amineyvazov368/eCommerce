package org.example.ecommers.exception.product;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(String message) {
        super("Product already exists: " + message);
    }
}
