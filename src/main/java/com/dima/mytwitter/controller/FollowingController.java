package com.dima.mytwitter.controller;

import com.dima.mytwitter.model.Following;
import com.dima.mytwitter.model.FollowingUsers;
import com.dima.mytwitter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("{user}/following")
public class FollowingController {

    private final String USER = "user";
    private final UserService userService;

    public FollowingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public FollowingUsers getFollowingUsers(@PathVariable(USER) String userName) {
        return new FollowingUsers(userService.getFollowingUsers(userName));
    }

    @PostMapping
    public ResponseEntity<String> addFollowingUser(
            @PathVariable(USER) String userName,
            @Valid @RequestBody Following following) {
        userService.addFollowingUser(userName, following.getUserName());
        return ResponseEntity.ok("Following user successfully added");
    }
}
