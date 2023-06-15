package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.service.ClientSuppressionService;
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

@WebMvcTest(ClientSuppressionsController.class)
public class ClientSuppressionsControllerTest {

    private static final String BASE_PATH = "/config/clients";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String SUPP_NAME = "supppressionName";

    @MockBean
    private ClientSuppressionService clientSuppressionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSuppressionIdentityResolutionConfiguration() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/suppressions";
        final List<Suppression> suppressions = getSuppressions();
        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, null)).thenReturn(suppressions);
        mockMvc.perform(get(endpointUri, CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions[0].name").value(suppressions.get(0).getName()));

        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, null)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void testGetSuppressionIdentityResolutionConfigurationWithSubClient() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/suppressions";
        final List<Suppression> suppressions = getSuppressions();
        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(suppressions);
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions[0].name").value(suppressions.get(0).getName()));

        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void updateSuppresion() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/suppressions";
        final List<Suppression> suppressions = getSuppressions();
        when(clientSuppressionService.updateClientSuppresion(getSuppression(), CLIENT_ID, null)).thenReturn(suppressions);

        MockHttpServletRequestBuilder content = put(endpointUri, CLIENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSuppression()));

        mockMvc.perform(content)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions[0].name").exists());
    }

    @Test
    void updateSuppresionThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/suppressions";
        when(clientSuppressionService.updateClientSuppresion(getSuppression(), CLIENT_ID, null)).thenThrow(new HttpConflictException(""));
        MockHttpServletRequestBuilder content = put(endpointUri, CLIENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getSuppression()));
        mockMvc.perform(content).andExpect(status().isConflict());
    }

    @Test
    void deleteSuppression() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/suppressions/{suppressionName}";
        final List<Suppression> suppressions = getSuppressions();
        when(clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, null)).thenReturn(suppressions);

        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUPP_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions[0].name").exists());
    }

    @Test
    void deleteSuppressionThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/suppressions/{suppressionName}";
        when(clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, null)).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUPP_NAME)).andExpect(status().isNotFound());
    }

    @Test
    void deleteWithSubclientSuppression() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/suppressions/{suppressionName}";
        final List<Suppression> suppressions = getSuppressions();
        when(clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(suppressions);

        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUB_CLIENT_ID, SUPP_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.suppressions[0].name").exists());
    }

    @Test
    void deleteWithSubclientSuppressionThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/suppressions/{suppressionName}";
        when(clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUB_CLIENT_ID, SUPP_NAME)).andExpect(status().isNotFound());
    }

    private List<Suppression> getSuppressions() {
        final List<Suppression> suppressions = new ArrayList<>();
        suppressions.add(getSuppression());
        return suppressions;
    }

    private Suppression getSuppression() {
        Suppression suppression = new Suppression();
        suppression.setName(SUPP_NAME);
        suppression.setEnabled(true);
        suppression.setEnableFuzzyGenderFilter(false);
        suppression.setResidentThreshold(92);
        suppression.setDateOfBirthRangeYears(999);
        return suppression;
    }
}
