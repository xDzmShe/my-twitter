package com.dima.mytwitter.service;

import com.dima.mytwitter.model.Message;
import com.dima.mytwitter.model.User;
import com.dima.mytwitter.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageServiceTest {

    private final String USERNAME = "user_name";

    @Mock
    private UserService userService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private DefaultMessageService service;

    @Test
    public void testPostMessage() {
        Message message = new Message("");
        service.postMessage(USERNAME, message);

        verify(userService, times(1)).getOrCreateUserByName(USERNAME);
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    public void testGetWallMessages() {
        User user = new User(USERNAME);
        when(userService.getAndValidateUserByName(USERNAME)).thenReturn(user);
        when(messageRepository.findAllByUserOrderByCreatedDateTimeDesc(user))
                .thenReturn(Arrays.asList(new Message("text1"), new Message("text2")));

        assertEquals(2, service.getWallMessages(USERNAME).size());
    }

    @Test
    public void getTimelineMessages() {
        User user = new User(USERNAME);

        user.setFollowings(
                new HashSet<>(Arrays.asList(new User("usr_1"), new User("usr_2"))));

        when(userService.getAndValidateUserByName(USERNAME)).thenReturn(user);
        when(messageRepository.findAllByUserInOrderByCreatedDateTimeDesc(user.getFollowings()))
                .thenReturn(Arrays.asList(new Message("text1"), new Message("text2"), new Message("text3")));

        assertEquals(3, service.getTimelineMessages(USERNAME).size());
    }
}