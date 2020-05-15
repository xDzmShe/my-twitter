package com.dima.mytwitter.model;

import lombok.Data;

import java.util.Collection;

@Data
public class FollowingUsers {
    private final Collection<User> followings;
}
