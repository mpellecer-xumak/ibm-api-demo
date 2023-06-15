package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.service.SuppressionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SuppressionsController.class)
class SuppressionsControllerTest {
    private static final String BASE_PATH = "/config/suppressions";
    private static final String SUPPRESSION_NAME = "MySuppression";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";

    @MockBean
    private SuppressionsService suppressionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeleteSuppression() throws Exception {
        final var request = delete(BASE_PATH + "/" + SUPPRESSION_NAME);
        mockMvc.perform(request).andExpect(status().is2xxSuccessful());
        verify(suppressionService).deleteSuppression(SUPPRESSION_NAME);
    }

    @Test
    void testDeleteSuppressionNotFound() throws Exception {
        final var request = delete(BASE_PATH + "/" + SUPPRESSION_NAME);
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(suppressionService).deleteSuppression(SUPPRESSION_NAME);
        final String response = mockMvc.perform(request).andExpect(status().isNotFound())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));

    }

    @Test
    void testDeleteSuppressionConflict() throws Exception {
        final var request = delete(BASE_PATH + "/" + SUPPRESSION_NAME);
        doThrow(new HttpConflictException(EXCEPTION_MESSAGE)).when(suppressionService).deleteSuppression(SUPPRESSION_NAME);
        final String response = mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
    }

    @Test
    void testUpdateSuppression() throws Exception {
        final Suppression suppression = new Suppression();
        suppression.setName(SUPPRESSION_NAME);
        suppression.setEnabled(Boolean.TRUE);
        doReturn(suppression).when(suppressionService).updateSuppression(suppression);
        mockMvc.perform(put(BASE_PATH)
                .content(new ObjectMapper().writeValueAsString(suppression))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SUPPRESSION_NAME))
            .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(Boolean.TRUE));
    }
    
    @Test
    void testUpdateSuppressionNotFound() throws Exception {
        final Suppression suppression = new Suppression();
        suppression.setName(SUPPRESSION_NAME);
        suppression.setEnabled(Boolean.TRUE);
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(suppressionService).updateSuppression(suppression);
        mockMvc.perform(put(BASE_PATH)
                .content(new ObjectMapper().writeValueAsString(suppression))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}
