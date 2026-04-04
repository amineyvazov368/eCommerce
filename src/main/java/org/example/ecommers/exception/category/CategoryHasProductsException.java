package org.example.ecommers.exception.category;

public class CategoryHasProductsException extends RuntimeException {
    public CategoryHasProductsException() {
        super("Category cannot be deleted because it has products");
    }
}
