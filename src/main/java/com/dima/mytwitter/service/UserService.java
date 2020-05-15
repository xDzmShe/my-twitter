package com.dima.mytwitter.service;

import com.dima.mytwitter.errorhandling.UserFollowingException;
import com.dima.mytwitter.errorhandling.UserNotFoundException;
import com.dima.mytwitter.model.User;

import java.util.Collection;

public interface UserService {
    User getAndValidateUserByName(String userName) throws UserNotFoundException;

    User getOrCreateUserByName(String userName);

    Collection<User> getFollowingUsers(String userName);

    void addFollowingUser(String currentUserName, String followingUserName) throws UserFollowingException;
}
