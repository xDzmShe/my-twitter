package com.dima.mytwitter.integration;

import com.dima.mytwitter.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class MessageControllerIntegrationTest extends AbstractIntegrationTest {
    /**
     * Test verifies that :
     * - user is created during posting first message
     * - all posted messages are present on the wall
     * - messages on the wall are sorted in reverse chronological order
     * - 404 Not Found is returned while trying to get wall for non-existing user
     */
    @Test
    void testPostMessage_successfullyPostedMessages() throws Exception {

        // verify that user is not present in db yet
        mockMvc.perform(get(WALL_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // post message for non-existing user
        postMessageSuccessfully("msg 0");

        // verify that user has been created during posting first message
        mockMvc.perform(get(WALL_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wall", hasSize(1)))
                .andExpect(jsonPath("$.wall[0].text", is("msg 0")));

        // post additional messages
        for (int i = 1; i <= 4; i++) {
            postMessageSuccessfully("msg " + i);
        }

        // verify that all messages are present in the wall in reverse chronological order
        mockMvc.perform(get(WALL_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wall", hasSize(5)))
                .andExpect(jsonPath("$.wall[*].text",
                        contains("msg 4", "msg 3", "msg 2", "msg 1", "msg 0")));
    }

    /**
     * Test verifies that posting message with empty text is not allowed
     */
    @Test
    void testPostMessage_emptyText() throws Exception {
        Message message = new Message("");

        mockMvc.perform(post(MESSAGE_URL)
                .content(objectMapper.writeValueAsString(message))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test verifies that posting message with more that 140 characters is not allowed
     */
    @Test
    void testPostMessage_textLengthMoreThan140chars() throws Exception {
        String veryLongString = "UT1ZtJ9sdzvLKuklIvtvfrG0FDaI" +
                "W1WBnSc10NpIUqwCAQdCwDeBt0gz9HDKjR5vkpz3rQTGLbVdn1fc9sd1OyE4" +
                "9fPflXkiAFraEr8RKhdYSrAfmHDX7jIzO3SRsXLbj3Ozi0XVN2H1yUH8KHJYOI";
        Message message = new Message(veryLongString);

        mockMvc.perform(post(MESSAGE_URL)
                .content(objectMapper.writeValueAsString(message))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test verifies that:
     * - timeline shows messages from following users
     * - messages on the timeline are sorted in reverse chronological order
     */
    @Test
    void testTimeline() throws Exception {
        // create few users and add messages
        postMessageSuccessfully("msg");
        postMessageSuccessfully("msg 2", USERNAME_2);
        postMessageSuccessfully("msg 3", USERNAME_3);

        // add following users
        addFollowingUserSuccessfully(USERNAME_2);
        addFollowingUserSuccessfully(USERNAME_3);

        // verify that timeline contains all messages from following users
        mockMvc.perform(get(TIMELINE_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timelineMessages", hasSize(2)))
                .andExpect(jsonPath("$.timelineMessages[*].text",
                        contains("msg 3", "msg 2")));

        // add few more messages
        postMessageSuccessfully("msg 22", USERNAME_2);
        postMessageSuccessfully("msg 33", USERNAME_3);
        postMessageSuccessfully("msg 222", USERNAME_2);

        // verify that timeline contains all messages from following users
        mockMvc.perform(get(TIMELINE_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timelineMessages", hasSize(5)))
                .andExpect(jsonPath("$.timelineMessages[*].text",
                        contains("msg 222", "msg 33", "msg 22", "msg 3", "msg 2")));
    }
}
