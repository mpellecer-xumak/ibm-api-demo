package com.wdm.configuration.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.HierarchyEnum;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import com.wdm.configuration.api.service.ClientMatcherService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientMatchersController.class)
public class ClientMatchersControllerTest {
    private static final String BASE_PATH = "/config/clients";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String MATCHER_NAME = "MyMatcher";
    private static final String HIERARCHY_NAME = "MyHierarchy";
    private static final Integer INDEX = 1;

    private static final String CLIENT_ID_PATH = BASE_PATH + "/{clientId}/matchers";
    private static final String CLIENT_SUBCLIENT_ID_PATH = BASE_PATH + "/{clientId}/{subClientId}/matchers";

    private static final String EXCEPTION_MESSAGE  = "exceptionMessage";

    @MockBean
    private ClientMatcherService clientMatcherService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetClientMatchers() throws Exception {
        final List<MatcherHierarchyGroup> machers = new ArrayList<>();
        final MatcherHierarchyGroup macher = new MatcherHierarchyGroup();
        macher.setHierarchy(HierarchyEnum.INSIGHT_HOUSEHOLD);
        macher.setMatcherDetails(new ArrayList<>());
        machers.add(macher);
        when(clientMatcherService.getMachersClientConfig(CLIENT_ID, null)).thenReturn(machers);
        mockMvc.perform(get(CLIENT_ID_PATH, CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers[0].hierarchy").value(HierarchyEnum.INSIGHT_HOUSEHOLD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers[0].matcherDetails").isArray());

        when(clientMatcherService.getMachersClientConfig(CLIENT_ID, null)).thenThrow(new HttpNotFoundException(EXCEPTION_MESSAGE));
        mockMvc.perform(get(CLIENT_ID_PATH, CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void testGetClientMatchersWithSubClient() throws Exception {
        final List<MatcherHierarchyGroup> machers = new ArrayList<>();
        final MatcherHierarchyGroup macher = new MatcherHierarchyGroup();
        macher.setHierarchy(HierarchyEnum.INSIGHT_HOUSEHOLD);
        macher.setMatcherDetails(new ArrayList<>());
        machers.add(macher);
        when(clientMatcherService.getMachersClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(machers);
        mockMvc.perform(get(CLIENT_SUBCLIENT_ID_PATH, CLIENT_ID, SUB_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers[0].hierarchy").value(HierarchyEnum.INSIGHT_HOUSEHOLD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchers[0].matcherDetails").isArray());

        when(clientMatcherService.getMachersClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenThrow(new HttpNotFoundException(EXCEPTION_MESSAGE));
        mockMvc.perform(get(CLIENT_SUBCLIENT_ID_PATH, CLIENT_ID, SUB_CLIENT_ID)).andExpect(status().isNotFound());
    }

    @Test
    void updateClientMatcher() throws Exception {
        final Matcher matcher = getMatcher();
        when(clientMatcherService.updateMatcher(CLIENT_ID, null, matcher)).thenReturn(matcher);
        mockMvc.perform(put(CLIENT_ID_PATH, CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(matcher)))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(matcher.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.hierarchy").value(matcher.getHierarchy()));
    }

    @Test
    void updateClientMatcherWithSubclient() throws Exception {
        final Matcher matcher = getMatcher();
        when(clientMatcherService.updateMatcher(CLIENT_ID, SUB_CLIENT_ID, matcher)).thenReturn(matcher);
        mockMvc.perform(put(CLIENT_SUBCLIENT_ID_PATH, CLIENT_ID, SUB_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(matcher)))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(matcher.getName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.hierarchy").value(matcher.getHierarchy()));
    }

    @Test
    void testDeleteClientMachers() throws Exception {
        final ClientMachersDeleteRequest deleteRequest = getClientMachersDeleteRequest();
        final var request = delete(CLIENT_SUBCLIENT_ID_PATH, CLIENT_ID, SUB_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deleteRequest));

        mockMvc.perform(request).andExpect(status().isOk());
        verify(clientMatcherService).deleteClientMatcher(CLIENT_ID, SUB_CLIENT_ID, deleteRequest);
    }

    @Test
    void testDeleteClientMachersNotFound() throws Exception {
        final ClientMachersDeleteRequest deleteRequest = getClientMachersDeleteRequest();
        final var request = delete(CLIENT_SUBCLIENT_ID_PATH, CLIENT_ID, SUB_CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(deleteRequest));;

        when(clientMatcherService.deleteClientMatcher(CLIENT_ID, SUB_CLIENT_ID, deleteRequest)).thenThrow(new HttpNotFoundException(EXCEPTION_MESSAGE));

        final String response = mockMvc.perform(request).andExpect(status().isNotFound())
                .andReturn().getResponse()
                .getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));

    }

    private ClientMachersDeleteRequest getClientMachersDeleteRequest() {
        final ClientMachersDeleteRequest deleteRequest = new ClientMachersDeleteRequest();
        deleteRequest.setMatcherName(MATCHER_NAME);
        deleteRequest.setHierarchyName(HIERARCHY_NAME);
        return deleteRequest;
    }

    private Matcher getMatcher() {
        final Matcher matcher = new Matcher();
        matcher.setName(MATCHER_NAME);
        matcher.setHierarchy(HIERARCHY_NAME);
        matcher.setIndex(INDEX);
        return matcher;
    }
}
