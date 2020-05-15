package com.dima.mytwitter.service;

import com.dima.mytwitter.model.Message;

import java.util.List;

public interface MessageService {
    void postMessage(String userName, Message message);

    List<Message> getWallMessages(String userName);

    List<Message> getTimelineMessages(String userName);
}
