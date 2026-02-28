package org.example.ecommers.exception.product;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
