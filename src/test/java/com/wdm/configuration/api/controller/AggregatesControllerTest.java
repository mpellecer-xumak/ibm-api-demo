package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.service.AggregateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AggregatesController.class)
public class AggregatesControllerTest {
    private static final String BASE_PATH = "/config/aggregates";
    private static final String AGGREGATE_NAME = "testAggregate";
    private static final String BUNDLE_NAME = "testBundle";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final Boolean enabled = false;
    @MockBean
    private AggregateService aggregateService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAggregate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var body = objectMapper.writeValueAsString(getRequest());
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, false, Collections.emptyList());
        when(aggregateService.createAggregate(any())).thenReturn(aggregate);
        final var request = post(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(body));
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateAggregate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var body = objectMapper.writeValueAsString(getRequest());
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, false, Collections.emptyList());
        when(aggregateService.createAggregate(any())).thenReturn(aggregate);

        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(body));

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllAggregates() throws Exception {
        final List<Aggregate<Bundle>> aggregates = getAggregates();

        when(aggregateService.getAllAggregates()).thenReturn(aggregates);
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates[0].bundles[0].name").exists());
    }

    @Test
    void testGetAllAggregatesThrow() throws Exception {
        when(aggregateService.getAllAggregates()).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAggregate() throws Exception {
        final var request = delete(BASE_PATH + "/" + AGGREGATE_NAME);
        mockMvc.perform(request).andExpect(status().is2xxSuccessful());
        verify(aggregateService).deleteAggregate(AGGREGATE_NAME);
    }

    @Test
    void testDeleteAggregateNotFound() throws Exception {
        final var request = delete(BASE_PATH + "/" + AGGREGATE_NAME);
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(aggregateService).deleteAggregate(AGGREGATE_NAME);
        final String response = mockMvc.perform(request).andExpect(status().isNotFound())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));

    }

    @Test
    void testDeleteAggregateConflict() throws Exception {
        final var request = delete(BASE_PATH + "/" + AGGREGATE_NAME);
        doThrow(new HttpConflictException(EXCEPTION_MESSAGE)).when(aggregateService).deleteAggregate(AGGREGATE_NAME);
        final String response = mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
    }

    private Aggregate<SimpleName> getRequest() {
        final SimpleName bundle = new SimpleName(BUNDLE_NAME);
        return new Aggregate<>(AGGREGATE_NAME, enabled, List.of(bundle));
    }

    private List<Aggregate<Bundle>> getAggregates() {
        final Bundle bundle = new Bundle(BUNDLE_NAME);
        Aggregate<Bundle> aggregate = new Aggregate<>(AGGREGATE_NAME, enabled, List.of(bundle));
        return List.of(aggregate);
    }

}

