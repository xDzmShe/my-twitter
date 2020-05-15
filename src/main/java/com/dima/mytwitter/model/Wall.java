package com.dima.mytwitter.model;

import lombok.Data;

import java.util.Collection;

@Data
public class Wall {
    private final Collection<Message> wall;
}
