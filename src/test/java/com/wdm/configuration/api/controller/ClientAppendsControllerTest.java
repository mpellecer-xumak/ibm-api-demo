package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.service.ClientAppendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientAppendsController.class)
public class ClientAppendsControllerTest {
    private static final String BASE_PATH = "/config/clients";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String APPEND_NAME = "append";

    @MockBean
    private ClientAppendService clientAppendService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAppendsIdentityResolutionConfiguration() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/appends";
        final List<Append> appends = getAppends();
        when(clientAppendService.getAppendsClientConfig(CLIENT_ID, null)).thenReturn(appends);
        mockMvc.perform(get(endpointUri, CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").exists());

        when(clientAppendService.getAppendsClientConfig(CLIENT_ID, null)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void testGetAppendsIdentityResolutionConfigurationWithSubClient() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/appends";
        final List<Append> appends = getAppends();
        when(clientAppendService.getAppendsClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(appends);
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").exists());

        when(clientAppendService.getAppendsClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void updateAppends() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/appends";
        final List<Append> appends = getAppends();
        when(clientAppendService.updateAssociatedAppend(CLIENT_ID, null, getAppend())).thenReturn(appends);

        MockHttpServletRequestBuilder content = put(endpointUri, CLIENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getAppend()));

        mockMvc.perform(content)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").exists());
    }

    @Test
    void updateAppendsThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/appends";
        when(clientAppendService.updateAssociatedAppend(CLIENT_ID, null, getAppend())).thenThrow(new HttpConflictException(""));
        MockHttpServletRequestBuilder content = put(endpointUri, CLIENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getAppend()));
         mockMvc.perform(content).andExpect(status().isConflict());
    }

    @Test
    void testRemoveClientsAppend() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/appends/{appendName}";
        final List<Append> appends = getAppends();
        when(clientAppendService.removeClientsAppend(CLIENT_ID, null, APPEND_NAME)).thenReturn(appends);

        mockMvc.perform(delete(endpointUri, CLIENT_ID, APPEND_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").exists());
    }

    @Test
    void testRemoveClientsAppendWithSubClient() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/appends/{appendName}";
        final List<Append> appends = getAppends();
        when(clientAppendService.removeClientsAppend(CLIENT_ID, SUB_CLIENT_ID, APPEND_NAME)).thenReturn(appends);

        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUB_CLIENT_ID, APPEND_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appends[0].name").exists());
    }

    @Test
    void testRemoveClientsAppendThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/appends/{appendName}";
        when(clientAppendService.removeClientsAppend(CLIENT_ID, null, APPEND_NAME)).thenThrow(new HttpNotFoundException(""));

        mockMvc.perform(delete(endpointUri, CLIENT_ID, APPEND_NAME))
                .andExpect(status().isNotFound());
    }

    private List<Append> getAppends() {
        final List<Append> appends = new ArrayList<>();
        appends.add(getAppend());
        return appends;
    }

    private Append getAppend() {
        Gson gson = new Gson();
        return gson.fromJson(getAppendJson(), Append.class);
    }

    private String getAppendJson() {
        return "{" +
                "\"name\": \"nameEmailToAddress\"," +
                "\"enabled\": true," +
                "\"suppressStateDoNotCall\": false," +
                "\"suppressFederalDoNotCall\": false," +
                "\"enableFuzzyGenderFilter\": false" +
                "}";
    }
}
