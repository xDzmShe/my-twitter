package com.dima.mytwitter.errorhandling;

public class UserFollowingException extends RuntimeException {
    public UserFollowingException(String message) {
        super(message);
    }
}
