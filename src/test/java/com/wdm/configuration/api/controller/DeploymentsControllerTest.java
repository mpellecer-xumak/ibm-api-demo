package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Deployment;
import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.service.VersionManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeploymentsController.class)
class DeploymentsControllerTest {
    private static final String ENTITY_NAME = "Email";
    private static final String BASE_PATH = "/config/deployments";
    private static final Deployment BLUE = Deployment.BLUE;
    public static final String BUILD_ID = "buildID";


    @MockBean
    private VersionManagementService managementService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDeployments() throws Exception {
        when(managementService.getDeploymentVersions()).thenReturn(List.of(getVersionManagement(null)));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deployments[0].entity").value(ENTITY_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deployments[0].state").value(BLUE.name()));
    }

    @Test
    void getDeployments_NotFound() throws Exception {
        when(managementService.getDeploymentVersions()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDeploymentState() throws Exception {
        when(managementService.getDeploymentVersion(ENTITY_NAME)).thenReturn(getVersionManagement(null));
        mockMvc.perform(get(BASE_PATH + "/" + ENTITY_NAME))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(BLUE.name()));
    }

    @Test
    void getDeploymentState_NotFound() throws Exception {
        when(managementService.getDeploymentVersion(ENTITY_NAME)).thenReturn(null);
        mockMvc.perform(get(BASE_PATH + "/" + ENTITY_NAME))
                .andExpect(status().isNotFound());
    }

    @Test
    void switchDeployment() throws Exception{
        when(managementService.getDeploymentVersion(ENTITY_NAME)).thenReturn(getVersionManagement(null));
        when(managementService.changeDeploymentVersion(ENTITY_NAME, null)).thenReturn(getVersionManagement(null));
        mockMvc.perform(put(BASE_PATH + "/" + ENTITY_NAME + "/switch"))
                .andExpect(status().isOk());
    }

    @Test
    void switchDeploymentWithBuild() throws Exception{
        when(managementService.getDeploymentVersion(ENTITY_NAME)).thenReturn(getVersionManagement(null));
        when(managementService.changeDeploymentVersion(ENTITY_NAME, BUILD_ID)).thenReturn(getVersionManagement(BUILD_ID));


        mockMvc.perform(put(BASE_PATH + "/" + ENTITY_NAME + "/switch?build=" + BUILD_ID))
                .andExpect(status().isOk());
    }

    private VersionManagement getVersionManagement(final String build) {
        return new VersionManagement(ENTITY_NAME, BLUE, build);
    }
}
