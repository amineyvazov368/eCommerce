package org.example.ecommers.exception.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }



}
