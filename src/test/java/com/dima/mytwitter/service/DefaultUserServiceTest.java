package com.dima.mytwitter.service;

import com.dima.mytwitter.errorhandling.UserFollowingException;
import com.dima.mytwitter.errorhandling.UserNotFoundException;
import com.dima.mytwitter.model.User;
import com.dima.mytwitter.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    private final String USERNAME = "user_name";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DefaultUserService service;

    @Test(expected = UserNotFoundException.class)
    public void testGetAndValidateUserByName_userNotFound() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.empty());
        service.getAndValidateUserByName(USERNAME);
    }

    @Test
    public void testGetAndValidateUserByName_foundUserReturned() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.of(new User(USERNAME)));
        User user = service.getAndValidateUserByName(USERNAME);
        assertEquals(USERNAME, user.getUserName());
    }

    @Test
    public void testGetOrCreateUserByName_createdUserReturned() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(USERNAME));

        User user = service.getOrCreateUserByName(USERNAME);
        assertEquals(USERNAME, user.getUserName());
    }

    @Test
    public void testGetOrCreateUserByName_foundUserReturned() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.of(new User(USERNAME)));

        User user = service.getOrCreateUserByName(USERNAME);
        assertEquals(USERNAME, user.getUserName());

        verify(userRepository, never()).save(any());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetFollowingUsers_userNotFound() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.empty());
        service.getFollowingUsers(USERNAME);
    }

    @Test
    public void testGetFollowingUsers_followingUsersReturned() {
        User userFromDb = new User(USERNAME);
        userFromDb.setFollowings(
                new HashSet<>(Arrays.asList(new User("usr_1"), new User("usr_2"))));

        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.of(userFromDb));
        Collection<User> followings = service.getFollowingUsers(USERNAME);
        assertEquals(2, followings.size());
    }

    @Test(expected = UserFollowingException.class)
    public void testAddFollowingUser_followHimself() {
        service.addFollowingUser(USERNAME, USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAddFollowingUser_currentUserNotFound() {
        service.addFollowingUser("non_existing", USERNAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAddFollowingUser_followingUserNotFound() {
        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.of(new User(USERNAME)));
        service.addFollowingUser(USERNAME, "following_user");
    }

    @Test
    public void testAddFollowingUser_followingUserAddedOnlyOnce() {
        final String followingUserName = "following_user";
        User currentUserFromDb = new User(USERNAME);

        when(userRepository.findByUserName(USERNAME)).thenReturn(Optional.of(currentUserFromDb));
        when(userRepository.findByUserName(followingUserName)).thenReturn(Optional.of(new User(followingUserName)));

        service.addFollowingUser(USERNAME, followingUserName);

        // add the same following user second time
        service.addFollowingUser(USERNAME, followingUserName);

        assertEquals(1, currentUserFromDb.getFollowings().size());
    }
}