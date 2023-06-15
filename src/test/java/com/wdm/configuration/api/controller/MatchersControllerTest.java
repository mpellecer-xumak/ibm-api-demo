package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.service.MatcherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchersController.class)
public class MatchersControllerTest {
    private static final String BASE_PATH = "/config/matchers";
    private static final String MATCHER_NAME = "testMatchers";
    private static final String EXCEPTION_MESSAGE  = "exceptionMessage";

    @MockBean
    private MatcherService matcherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeleteMatcher() throws Exception {
        final String endpointUri = BASE_PATH + "/{matcherName}";
        final MockHttpServletRequestBuilder content = delete(endpointUri, MATCHER_NAME);
        mockMvc.perform(content).andExpect(status().isOk());
        verify(matcherService).deleteMatcher(MATCHER_NAME);
    }

    @Test
    void testDeleteMatcherNotFound() throws Exception {
        final String endpointUri = BASE_PATH + "/{matcherName}";
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(matcherService).deleteMatcher(MATCHER_NAME);
        final MockHttpServletRequestBuilder content = delete(endpointUri, MATCHER_NAME);
        final String response = mockMvc.perform(content).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
    }

    @Test
    void testDeleteMatcherConflict() throws Exception {
        final String endpointUri = BASE_PATH + "/{matcherName}";
        doThrow(new HttpConflictException(EXCEPTION_MESSAGE)).when(matcherService).deleteMatcher(MATCHER_NAME);
        final MockHttpServletRequestBuilder content = delete(endpointUri, MATCHER_NAME);
        final String response = mockMvc.perform(content).andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
    }
}
