package com.dima.mytwitter.repository;

import com.dima.mytwitter.model.Message;
import com.dima.mytwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByUserOrderByCreatedDateTimeDesc(User user);

    List<Message> findAllByUserInOrderByCreatedDateTimeDesc(Collection<User> user);
}
