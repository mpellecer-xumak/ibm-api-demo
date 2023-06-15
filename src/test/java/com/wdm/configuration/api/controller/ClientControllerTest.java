package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.service.ClientConfigService;
import com.wdm.configuration.api.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientsController.class)
public class ClientControllerTest {
    private static final String BASE_PATH = "/config/clients";

    @MockBean
    private ClientService clientService;
    @MockBean
    private ClientConfigService clientConfigService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetClients() throws Exception {
        final ClientConfig client = new ClientConfig();
        when(clientService.getAllClients()).thenReturn(List.of(client));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClientsNotFound() throws Exception {
        doThrow(new HttpNotFoundException("Exception")).when(clientService).getAllClients();
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

}
