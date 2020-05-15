package com.dima.mytwitter.integration;

import com.dima.mytwitter.model.Following;
import com.dima.mytwitter.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    protected final static String USERNAME = "user_first";
    protected final static String USERNAME_2 = "user_second";
    protected final static String USERNAME_3 = "user_third";


    protected final static String MESSAGE_URL = "/" + USERNAME + "/message";
    protected final static String WALL_URL = "/" + USERNAME + "/wall";
    protected final static String FOLLOWING_URL = "/" + USERNAME + "/following";
    protected final static String TIMELINE_URL = "/" + USERNAME + "/timeline";

    protected final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    protected void postMessageSuccessfully(String text, String userName) throws Exception {
        Message message = new Message(text);

        mockMvc.perform(post("/" + userName + "/message")
                .content(objectMapper.writeValueAsString(message))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected void postMessageSuccessfully(String text) throws Exception {
        postMessageSuccessfully(text, USERNAME);
    }

    protected void addFollowingUserSuccessfully(String followingUserName) throws Exception {

        mockMvc.perform(post(FOLLOWING_URL)
                .content(objectMapper.writeValueAsString(new Following(followingUserName)))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected void postThreeMessagesForThreeDifferentUsers() throws Exception {
        postMessageSuccessfully("msg");
        postMessageSuccessfully("msg 2", USERNAME_2);
        postMessageSuccessfully("msg 3", USERNAME_3);
    }
}
