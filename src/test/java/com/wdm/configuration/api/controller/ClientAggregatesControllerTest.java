package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.service.ClientAggregateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientAggregatesController.class)
public class ClientAggregatesControllerTest {
    private static final String BASE_PATH = "/config/clients";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";

    private final String AGGREGATE_NAME = "myAggregate";
    private final String BUNDLE_NAME = "myBundle";
    private final String BFP_BUNDLE = "myBundle";

    @MockBean
    private ClientAggregateService clientAggregateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAggregatesIdentityResolutionConfiguration() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/aggregates";
        final List<Aggregate<Bundle>> aggregates = getAggregates();
        when(clientAggregateService.getClientAggregates(CLIENT_ID, null)).thenReturn(aggregates);
        mockMvc.perform(get(endpointUri, CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates[0].name").exists());

        when(clientAggregateService.getClientAggregates(CLIENT_ID, null)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void testGetAggregatesIdentityResolutionConfigurationWithSubClient() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/aggregates";
        final List<Aggregate<Bundle>> aggregates = getAggregates();
        when(clientAggregateService.getClientAggregates(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(aggregates);
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates[0].name").exists());

        when(clientAggregateService.getClientAggregates(CLIENT_ID, SUB_CLIENT_ID)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(get(endpointUri, CLIENT_ID, SUB_CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAggregatesIdentityResolutionConfiguration() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/aggregates/{aggregateName}";
        when(clientAggregateService.removeAggregateFromClient(CLIENT_ID, null, AGGREGATE_NAME)).thenReturn(Collections.emptyList());
        mockMvc.perform(delete(endpointUri, CLIENT_ID, AGGREGATE_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates", hasSize(0)));

        when(clientAggregateService.removeAggregateFromClient(CLIENT_ID, null, AGGREGATE_NAME)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(delete(endpointUri, CLIENT_ID, AGGREGATE_NAME)).andExpect(status().isNotFound());
    }
    @Test
    void testDeleteAggregatesIdentityResolutionConfigurationWithSubClient() throws Exception {
        final String endpointUri = BASE_PATH + "/{clientId}/{subClientId}/aggregates/{aggregateName}";
        when(clientAggregateService.removeAggregateFromClient(CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME)).thenReturn(Collections.emptyList());
        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.aggregates", hasSize(0)));

        when(clientAggregateService.removeAggregateFromClient(CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME)).thenThrow(new HttpNotFoundException("Client does not have appends associated"));
        mockMvc.perform(delete(endpointUri, CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME)).andExpect(status().isNotFound());
    }

    private List<Aggregate<Bundle>> getAggregates() {
        final List<Aggregate<Bundle>> aggregates = new ArrayList<>();
        Aggregate<Bundle> aggregate = new Aggregate<>();
        aggregate.setName(AGGREGATE_NAME);
        aggregate.setEnabled(true);
        Bundle bundle = new Bundle();
        bundle.setName(BUNDLE_NAME);
        aggregate.setBundles(Collections.singletonList(bundle));
        aggregates.add(aggregate);
        return aggregates;
    }
}
