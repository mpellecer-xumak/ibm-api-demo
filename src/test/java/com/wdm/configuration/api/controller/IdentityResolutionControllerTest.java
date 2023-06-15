package com.wdm.configuration.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdm.configuration.api.request.IdentityRequest;
import com.wdm.configuration.api.service.IdentityResolutionService;

@WebMvcTest(IdentityResolutionController.class)
public class IdentityResolutionControllerTest {

    private static final String BASE_PATH = "/config/platforms/identity-resolution";
    @MockBean
    private IdentityResolutionService identityResolutionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getIdentityResolution() throws Exception {
        when(identityResolutionService.getIdentityResolutionConfiguration()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk());
    }
    @Test
    void getIdentityResolutionNotFound() throws Exception {
        when(identityResolutionService.getIdentityResolutionConfiguration()).thenReturn(null);
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutIdentityResolutionConfig() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        final IdentityRequest config = new IdentityRequest(1,1,1,1,1,1,1,1,1,false,false,false,false,false,false);
        final var requestBody= objectMapper.writeValueAsString(config);
        when(identityResolutionService.putProvidedConfiguration(config)).thenReturn(config);
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        final var result = identityResolutionService.putProvidedConfiguration(config);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchLimit").value(config.getMatchLimit()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastNameThreshold").value(config.getLastNameThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.residentThreshold").value(config.getResidentThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.individualThreshold").value(config.getResidentThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.householdThreshold").value(config.getHouseholdThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressThreshold").value(config.getAddressThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirthRangeYears").value(config.getDateOfBirthRangeYears()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.individualThreshold").value(config.getResidentThreshold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stdAddressEnabled").value(config.getStdAddressEnabled()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.useGraphId").value(config.getUseGraphId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableKeyring").value(config.getEnableKeyring()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableFuzzyGenderFilter").value(config.getEnableFuzzyGenderFilter()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dpvStatusEnabled").value(config.getDpvStatusEnabled()));
        assertNotNull(result);
    }

    @Test
    void testPutIdentityResolutionConfigError() throws Exception{
        final ObjectMapper objectMapper = new ObjectMapper();
        final IdentityRequest config = new IdentityRequest(1,1,1,1,1,1,1,1,1,false,false,false,false,false,false);
        final var requestBody= objectMapper.writeValueAsString(config);
        when(identityResolutionService.putProvidedConfiguration(config)).thenReturn(null);
        final var request = put(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        mockMvc.perform(request).andExpect(status().isConflict());
    }

}
