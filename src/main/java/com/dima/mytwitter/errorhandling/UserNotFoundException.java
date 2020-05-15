package com.dima.mytwitter.errorhandling;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userName) {
        super("User " + userName + " not found!");
    }
}
