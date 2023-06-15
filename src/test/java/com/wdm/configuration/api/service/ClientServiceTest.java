package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.wdm.configuration.api.mapper.fromdbentity.DbClientToClientMapper;
import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.todbentity.ClientConfigToDbConfigMapper;
import com.wdm.configuration.api.mapper.todbentity.ClientToClientDbMapper;
import com.wdm.configuration.api.model.PlatformConfig;
import com.wdm.configuration.api.persistence.repository.ClientRepository;
import com.wdm.configuration.api.request.ClientCreationRequest;
import com.wdm.configuration.api.request.ClientUpdateRequest;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final int ADDRESS_THRESHOLD = 10;
    private static final int UPDATED_ADDRESS_THRESHOLD = 15;
    private static final String BATCH_BUNDLE = "batchBundle";
    private static final String UPDATED_BATCH_BUNDLE = "updatedBatchBundle";
    private static final String KEY = "key";

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientToClientDbMapper dbClientMapper;
    @Mock
    private ClientConfigToDbConfigMapper dbClientConfigMapper;
    @Mock
    private DbClientToClientMapper dbClientToClientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testCreateClient() {
        final var platformConfig = new PlatformConfig();
        platformConfig.setAddressThreshold(ADDRESS_THRESHOLD);
        final var clientCreationRequest = ClientCreationRequest.builder()
                .clientId(CLIENT_ID)
                .subClientId(SUB_CLIENT_ID)
                .config(platformConfig)
                .build();
        final var dbConfig = new DbIdentityResolutionClientConfig();
        dbConfig.setAddressThreshold(ADDRESS_THRESHOLD);
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);
        when(dbClientMapper.map(clientCreationRequest)).thenReturn(new DbClient());
        when(dbClientConfigMapper.map(platformConfig)).thenReturn(dbConfig);
        when(clientRepository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        final var result = clientService.createClient(clientCreationRequest);
        assertEquals(ADDRESS_THRESHOLD, result.getClientConfig().getAddressThreshold());
    }

    @Test
    void testCreateClient_ClientAlreadyExist() {
        final var platformConfig = new PlatformConfig();
        platformConfig.setAddressThreshold(ADDRESS_THRESHOLD);
        final var clientCreationRequest = ClientCreationRequest.builder()
                .clientId(CLIENT_ID)
                .subClientId(SUB_CLIENT_ID)
                .config(platformConfig)
                .build();
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(new DbClient());

        assertThrows(HttpConflictException.class, () -> clientService.createClient(clientCreationRequest));

        verifyNoInteractions(dbClientMapper, dbClientConfigMapper);
    }

    @Test
    void testGetClient() {
        final var dbClient = new DbClient();
        dbClient.setClientId(CLIENT_ID);
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, null)).thenReturn(dbClient);

        final var result = clientService.getClient(CLIENT_ID, null);
        assertEquals(CLIENT_ID, result.getClientId());
        assertNull(result.getSubClientId());
    }

    @Test
    void testGetClient_ClientNotFound() {
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, null)).thenReturn(null);

        assertThrows(HttpNotFoundException.class, () -> clientService.getClient(CLIENT_ID, null));
    }

    @Test
    void testUpdateClient_ClientInfo() {
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, null)).thenReturn(getClient());
        when(clientRepository.save(any(DbClient.class))).then(a -> a.getArguments()[0]);
        final ClientUpdateRequest creationRequest = new ClientUpdateRequest();
        creationRequest.setBatchFileProcessingBundle(UPDATED_BATCH_BUNDLE);
        final DbClient client = clientService.updateClient(CLIENT_ID, null, creationRequest);

        assertEquals(UPDATED_BATCH_BUNDLE, client.getBatchFileProcessingBundle());
        assertEquals(ADDRESS_THRESHOLD, client.getClientConfig().getAddressThreshold());
    }

    @Test
    void testUpdateClient_ClientConfigChanges() {
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, null)).thenReturn(getClient());
        when(clientRepository.save(any(DbClient.class))).then(a -> a.getArguments()[0]);
        final ClientUpdateRequest creationRequest = new ClientUpdateRequest();
        final PlatformConfig config = new PlatformConfig();
        config.setAddressThreshold(UPDATED_ADDRESS_THRESHOLD);
        creationRequest.setConfig(config);
        final DbClient client = clientService.updateClient(CLIENT_ID, null, creationRequest);

        assertEquals(UPDATED_ADDRESS_THRESHOLD, client.getClientConfig().getAddressThreshold());
    }

    @Test
    void testUpdateClient_NewConfig() {
        final DbClient dbClient = getClient();
        dbClient.setClientConfig(null);
        when(clientRepository.findByClientIdAndSubClientId(CLIENT_ID, null)).thenReturn(dbClient);
        when(clientRepository.save(any(DbClient.class))).then(a -> a.getArguments()[0]);
        when(dbClientConfigMapper.map(any(PlatformConfig.class))).thenAnswer(a -> {
            final DbIdentityResolutionClientConfig dbConfig = new DbIdentityResolutionClientConfig();
            final PlatformConfig platformConfig = (PlatformConfig) a.getArguments()[0];
            dbConfig.setAddressThreshold(platformConfig.getAddressThreshold());
            return dbConfig;
        });
        final ClientUpdateRequest creationRequest = new ClientUpdateRequest();
        final PlatformConfig config = new PlatformConfig();
        config.setAddressThreshold(UPDATED_ADDRESS_THRESHOLD);
        creationRequest.setConfig(config);
        final DbClient client = clientService.updateClient(CLIENT_ID, null, creationRequest);

        assertEquals(UPDATED_ADDRESS_THRESHOLD, client.getClientConfig().getAddressThreshold());
    }

    @Test
    void testGetAllClients() {
        final DbClient dbClient = getClient();
        when(clientRepository.findAll()).thenReturn(List.of(dbClient));
        final List<ClientConfig> result = clientService.getAllClients();
        assertNotNull(result);
    }

    @Test
    void testGetAllClientsNotFound() {
        when(clientRepository.findAll()).thenReturn(null);
        final Throwable exception = assertThrows(HttpNotFoundException.class, () -> clientService.getAllClients());
        assertEquals("404 No clients were found", exception.getMessage());
    }

    private DbClient getClient() {
        final DbClient client = new DbClient();
        client.setClientId(CLIENT_ID);
        client.setBatchFileProcessingBundle(BATCH_BUNDLE);
        client.setKey(KEY);
        final DbIdentityResolutionClientConfig clientConfig = new DbIdentityResolutionClientConfig();
        clientConfig.setAddressThreshold(ADDRESS_THRESHOLD);
        client.setClientConfig(clientConfig);
        return client;
    }
}
