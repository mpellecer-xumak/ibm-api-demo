package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import com.wdm.configuration.api.service.PlatformService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlatformsController.class)
public class PlatformsControllerTest {
    private static final String BASE_PATH = "/config/platforms/matchers";
    private static final String MATCHER_NAME = "myMatcher";
    private static final String HIERARCHY_NAME = "myHierarchy";
    private static final String EXCEPTION_MESSAGE  = "exceptionMessage";

    @MockBean
    private PlatformService platformService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreatePlatformMatcher() throws Exception {
        final var requestBody = getMatcher();
        final var request = post(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(platformService.createPlatformMatcher(requestBody)).thenReturn(requestBody);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(MATCHER_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hierarchy").value(HIERARCHY_NAME));

    }

    @Test
    void testUpdatePlatformMatcher() throws Exception {
        final var requestBody = getMatcher();
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(platformService.updatePlatformMatcher(requestBody)).thenReturn(requestBody);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(MATCHER_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hierarchy").value(HIERARCHY_NAME));
    }

    @Test
    void testUpdatePlatformMatcher_WhenNotFoundException() throws Exception {
        final var requestBody = getMatcher();
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(platformService.updatePlatformMatcher(requestBody)).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPlatformMatchers() throws Exception {
        final List<PlatformMatcher> platformMatchers = getPlatformMatchers();
        when(platformService.getPlatformMatchers()).thenReturn(platformMatchers);
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers[0].hierarchy").value(platformMatchers.get(0).getHierarchy()));
    }

    @Test
    void testDeleteClientMachers() throws Exception {
        final String endpointUri = BASE_PATH + "/{matcherName}/{hierarchyName}";
        final var request = delete(endpointUri, MATCHER_NAME, HIERARCHY_NAME);

        mockMvc.perform(request).andExpect(status().isOk());
        verify(platformService).deletePlatformMatchers(MATCHER_NAME, HIERARCHY_NAME);
    }

    @Test
    void testDeleteClientMachersNotFound() throws Exception {
        final String endpointUri = BASE_PATH + "/{matcherName}/{hierarchyName}";
        final var request = delete(endpointUri, MATCHER_NAME, HIERARCHY_NAME);

        when(platformService.deletePlatformMatchers(MATCHER_NAME, HIERARCHY_NAME)).thenThrow(new HttpNotFoundException(EXCEPTION_MESSAGE));

        final String response = mockMvc.perform(request).andExpect(status().isNotFound())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));

    }

    private Matcher getMatcher() {
        final Matcher matcher = new Matcher();
        matcher.setName(MATCHER_NAME);
        matcher.setHierarchy(HIERARCHY_NAME);
        return matcher;
    }

    private List<PlatformMatcher> getPlatformMatchers() {
        final List<PlatformMatcher> platformMatchers = new ArrayList<>();
        final List<Matcher> matchers = new ArrayList<>();
        final Matcher matcher = new Matcher(MATCHER_NAME, 1, true, 1);
        matchers.add(matcher);
        final PlatformMatcher platformMatcher = new PlatformMatcher(HIERARCHY_NAME, matchers);
        platformMatchers.add(platformMatcher);
        return platformMatchers;
    }
}
