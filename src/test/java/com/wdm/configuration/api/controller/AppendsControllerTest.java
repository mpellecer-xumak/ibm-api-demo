package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.service.AppendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppendsController.class)
public class AppendsControllerTest {
    private static final String BASE_PATH = "/config/appends";
    private static final String APPEND_NAME = "MyAppend";
    private static final Integer ADDRESS_THRESHOLD = 11;
    private static final String EXCEPTION_MESSAGE  = "exceptionMessage";


    @MockBean
    private AppendService appendService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAppend() throws Exception {
        final var requestBody = getAppend();
        final var request = post(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(appendService.createAppend(requestBody)).thenReturn(requestBody);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(APPEND_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressThreshold").value(ADDRESS_THRESHOLD));
    }

    @Test
    void testDeleteAppend() throws Exception {
        final var request = delete(BASE_PATH + "/" + APPEND_NAME);
        mockMvc.perform(request).andExpect(status().is2xxSuccessful());
        verify(appendService).deleteAppend(APPEND_NAME);
    }

    @Test
    void testDeleteAppendNotFound() throws Exception {
        final var request = delete(BASE_PATH + "/" + APPEND_NAME);
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(appendService).deleteAppend(APPEND_NAME);
        final String response = mockMvc.perform(request).andExpect(status().isNotFound())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));

    }

    @Test
    void testDeleteAppendConflict() throws Exception {
        final var request = delete(BASE_PATH + "/" + APPEND_NAME);
        doThrow(new HttpConflictException(EXCEPTION_MESSAGE)).when(appendService).deleteAppend(APPEND_NAME);
        final String response = mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
    }

    @Test
    void testUpdateAppend() throws Exception {
        final Append requestBody = new Append();
        requestBody.setName(APPEND_NAME);
        requestBody.setEnableFuzzyGenderFilter(true);
        requestBody.setSuppressFederalDoNotCall(true);
        requestBody.setSuppressStateDoNotCall(false);
        requestBody.setAddressThreshold(ADDRESS_THRESHOLD);
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(appendService.updateAppend(requestBody)).thenReturn(requestBody);
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(APPEND_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressThreshold").value(ADDRESS_THRESHOLD));
    }

    @Test
    void testUpdateAppendConflict() throws Exception {
        final Append requestBody = new Append();
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody));
        when(appendService.updateAppend(requestBody)).thenThrow(new HttpNotFoundException(EXCEPTION_MESSAGE));
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAppends() throws Exception {
        when(appendService.getAppends()).thenReturn(List.of(getAppend()));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").value(APPEND_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].addressThreshold").value(ADDRESS_THRESHOLD));
    }

    @Test
    void testGetAppends_WhenNoAppendsFound() throws Exception {
        when(appendService.getAppends()).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

    private Append getAppend() {
        final Append append = new Append();
        append.setName(APPEND_NAME);
        append.setEnableFuzzyGenderFilter(true);
        append.setSuppressFederalDoNotCall(true);
        append.setSuppressStateDoNotCall(false);
        append.setAddressThreshold(ADDRESS_THRESHOLD);
        return append;
    }

}
