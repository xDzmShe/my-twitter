package com.dima.mytwitter.service;

import com.dima.mytwitter.model.Message;
import com.dima.mytwitter.model.User;
import com.dima.mytwitter.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public DefaultMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public void postMessage(String userName, Message message) {
        message.setUser(userService.getOrCreateUserByName(userName));
        message.setCreatedDateTime(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    public List<Message> getWallMessages(String userName) {
        User user = userService.getAndValidateUserByName(userName);
        return messageRepository.findAllByUserOrderByCreatedDateTimeDesc(user);
    }

    @Override
    public List<Message> getTimelineMessages(String userName) {
        User user = userService.getAndValidateUserByName(userName);
        return messageRepository.findAllByUserInOrderByCreatedDateTimeDesc(user.getFollowings());
    }
}
