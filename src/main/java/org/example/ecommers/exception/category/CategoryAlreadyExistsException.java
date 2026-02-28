package org.example.ecommers.exception.category;

public class CategoryAlreadyExistsException extends RuntimeException{

    public CategoryAlreadyExistsException(String message) {
        super("Category already exists: " + message);
    }
}
