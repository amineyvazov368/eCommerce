package org.example.ecommers.exception.user;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super("User " + message + " already exists");
    }
}
