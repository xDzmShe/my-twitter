package com.dima.mytwitter.service;

import com.dima.mytwitter.errorhandling.UserFollowingException;
import com.dima.mytwitter.errorhandling.UserNotFoundException;
import com.dima.mytwitter.model.User;
import com.dima.mytwitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getAndValidateUserByName(String userName) throws UserNotFoundException {
        return getUserByName(userName).orElseThrow(() -> new UserNotFoundException(userName));
    }

    @Override
    public User getOrCreateUserByName(String userName) {
        return getUserByName(userName).orElseGet(() -> userRepository.save(new User(userName)));
    }

    @Override
    public Collection<User> getFollowingUsers(String userName) {
        return getAndValidateUserByName(userName).getFollowings();
    }

    @Override
    @Transactional
    public void addFollowingUser(String currentUserName, String followingUserName) throws UserFollowingException {
        if (currentUserName.equals(followingUserName)) {
            throw new UserFollowingException("User cannot follow himself");
        }

        User currentUser = getAndValidateUserByName(currentUserName);
        User followingUser = getAndValidateUserByName(followingUserName);
        currentUser.getFollowings().add(followingUser);
    }

    private Optional<User> getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
