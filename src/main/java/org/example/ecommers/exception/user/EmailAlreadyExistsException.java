package org.example.ecommers.exception.user;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super("Email already exists: " + message);
    }
}
