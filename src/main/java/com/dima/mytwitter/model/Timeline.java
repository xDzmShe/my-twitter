package com.dima.mytwitter.model;

import lombok.Data;

import java.util.Collection;

@Data
public class Timeline {
    private final Collection<Message> timelineMessages;
}
