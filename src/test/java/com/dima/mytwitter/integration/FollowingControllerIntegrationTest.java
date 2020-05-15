package com.dima.mytwitter.integration;

import com.dima.mytwitter.model.Following;
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
public class FollowingControllerIntegrationTest extends AbstractIntegrationTest {

    /**
     * Test verifies that :
     * - by default user has empty list of following users
     * - duplicated items are not added to list of following users
     * - 404 Not Found is returned while trying to get list of following users for non-existing user
     */
    @Test
    void testFollowing_successfullyAddedFollowingUsers() throws Exception {
        // verify that user is not present in db yet
        mockMvc.perform(get(FOLLOWING_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // add few users
        postThreeMessagesForThreeDifferentUsers();

        // verify that user has empty list of following users
        mockMvc.perform(get(FOLLOWING_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followings", empty()));

        // add following user
        addFollowingUserSuccessfully(USERNAME_2);

        // verify that user has one added following users
        mockMvc.perform(get(FOLLOWING_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followings", hasSize(1)))
                .andExpect(jsonPath("$.followings[0].userName", is(USERNAME_2)));

        // add another following user
        addFollowingUserSuccessfully(USERNAME_3);

        // add following user that is already in the list
        addFollowingUserSuccessfully(USERNAME_3);

        // verify that list of following users contains 2 unique items (no duplicates)
        mockMvc.perform(get(FOLLOWING_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followings", hasSize(2)))
                .andExpect(jsonPath("$.followings[*].userName", hasItems(USERNAME_2, USERNAME_3)));
    }

    /**
     * Test verifies that cannot get list of following users for non-existing user
     */
    @Test
    void testFollowing_cannotGetFollowingUsersForNonExistingUser() throws Exception {
        // verify that user is not present in db yet
        mockMvc.perform(get(FOLLOWING_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test verifies that user cannot follow himself
     */
    @Test
    void testFollowing_cannotFollowHimself() throws Exception {
        postMessageSuccessfully("msg");
        Following following = new Following(USERNAME);

        // verify that user cannot follow himself
        mockMvc.perform(post(FOLLOWING_URL)
                .content(objectMapper.writeValueAsString(following))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
