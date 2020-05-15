package com.dima.mytwitter.controller;

import com.dima.mytwitter.model.Message;
import com.dima.mytwitter.model.Timeline;
import com.dima.mytwitter.model.Wall;
import com.dima.mytwitter.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("{user}")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/message")
    public ResponseEntity<String> postMessage(
            @PathVariable("user") String userName,
            @Valid @RequestBody Message message) {
        messageService.postMessage(userName, message);
        return ResponseEntity.ok("Message successfully posted");
    }

    @GetMapping("/wall")
    public Wall getWallMessages(@PathVariable("user") String userName) {
        return new Wall(messageService.getWallMessages(userName));
    }

    @GetMapping("/timeline")
    public Timeline getTimelineMessages(@PathVariable("user") String userName) {
        return new Timeline(messageService.getTimelineMessages(userName));
    }
}
