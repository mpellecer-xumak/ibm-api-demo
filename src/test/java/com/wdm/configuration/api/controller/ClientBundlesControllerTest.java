package com.wdm.configuration.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.request.BundlesRequest;
import com.wdm.configuration.api.service.BundlesService;
import org.codehaus.plexus.util.cli.Arg;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientBundlesController.class)
public class ClientBundlesControllerTest {
    private static final String BASE_PATH = "/config/clients";
    private static final String BUNDLES_ENDPOINT = "/bundles";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String BUNDLE_NAME = "myBundle";


    @MockBean
    private BundlesService bundlesService;


    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAssociateHumanInsights() throws Exception {
        final var br = getRequest();
        when(bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, null, br.getBundles()))
                        .thenReturn(getBundles());
        final var request = post(BASE_PATH + "/" + CLIENT_ID + BUNDLES_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(br));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundles[0].name").value(BUNDLE_NAME));
    }

    @Test
    void testAssociateHumanInsights_WithSubClient() throws Exception {
        final BundlesRequest br = getRequest();
        when(bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID, br.getBundles()))
                        .thenReturn(getBundles());
        final var request = post(BASE_PATH + "/" + CLIENT_ID + "/" + SUB_CLIENT_ID + BUNDLES_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(br));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(SUB_CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundles[0].name").value(BUNDLE_NAME));
    }

    static Stream<Arguments> subClientDataProvider() {
        return Stream.of(
                Arguments.of(
                        SUB_CLIENT_ID,
                        BASE_PATH + "/" + CLIENT_ID + "/" + SUB_CLIENT_ID + BUNDLES_ENDPOINT),
                Arguments.of(
                        null,
                        BASE_PATH + "/" + CLIENT_ID + BUNDLES_ENDPOINT)
        );
    }

    @DisplayName("Delete client/sub client bundle test case")
    @ParameterizedTest
    @MethodSource("subClientDataProvider")
    void testDeleteHumanInsights(String subClient, String endPoint) throws Exception {
        final var bundlesRequest = getRequest();
        when(bundlesService.removeClientBundles(CLIENT_ID, subClient, bundlesRequest.getBundles()))
                .thenReturn(getBundles());
        final var request = delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(bundlesRequest));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subClientId").value(subClient))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundles[0].name").value(BUNDLE_NAME));
    }

    @DisplayName("Delete client bundle using non-existing client")
    @ParameterizedTest
    @MethodSource("subClientDataProvider")
    void testDeleteHumanInsights_ExceptNotFound(String subClient, String endPoint) throws Exception {
        final var notFoundMessage = "Not Found";
        final var bundlesRequest = getRequest();

        when(bundlesService.removeClientBundles(CLIENT_ID, subClient, bundlesRequest.getBundles()))
                .thenThrow(new HttpNotFoundException(notFoundMessage));

        final var request = delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToString(bundlesRequest));
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(containsString(notFoundMessage)))
                .andDo(print());
    }


    private String convertObjectToString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private BundlesRequest getRequest() {
        final SimpleName bundle = new SimpleName(BUNDLE_NAME);
        return BundlesRequest.builder()
                .bundles(List.of(bundle))
                .build();
    }

    private List<Bundle> getBundles() {
            return List.of(new Bundle(BUNDLE_NAME));
    }
}
