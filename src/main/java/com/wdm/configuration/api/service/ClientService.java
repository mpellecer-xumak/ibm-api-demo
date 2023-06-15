package com.wdm.configuration.api.service;

import com.wdm.configuration.api.mapper.fromdbentity.DbClientToClientMapper;
import com.wdm.configuration.api.model.ClientConfig;
import org.springframework.stereotype.Service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.todbentity.ClientConfigToDbConfigMapper;
import com.wdm.configuration.api.mapper.todbentity.ClientToClientDbMapper;
import com.wdm.configuration.api.model.PlatformConfig;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolutionClientConfig;
import com.wdm.configuration.api.persistence.repository.ClientRepository;
import com.wdm.configuration.api.request.ClientCreationRequest;
import com.wdm.configuration.api.request.ClientUpdateRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientToClientDbMapper dbClientMapper;
    private final ClientConfigToDbConfigMapper dbClientConfigMapper;
    private final DbClientToClientMapper dbClientToClientMapper;

    public List<ClientConfig> getAllClients() {
        List<DbClient> dbClientList = clientRepository.findAll();
        if (CollectionUtils.isEmpty(dbClientList)) {
            final String message = "No clients were found";
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return dbClientToClientMapper.map(dbClientList);
    }

    public DbClient createClient(final ClientCreationRequest clientRequest) {
        final DbClient existingClient = clientRepository.findByClientIdAndSubClientId(clientRequest.getClientId(),
                clientRequest.getSubClientId());
        if (existingClient != null) {
            final String message = "Trying to create a client that already exists";
            log.warn(message, clientRequest.getClientId(), clientRequest.getSubClientId());
            throw new HttpConflictException(message);
        }
        final DbClient client = dbClientMapper.map(clientRequest);
        assignIrConfig(client, clientRequest.getConfig());
        return save(client);
    }

    public DbClient updateClient(final String clientId, final String subClientId, final ClientUpdateRequest clientRequest) {
        final DbClient existingClient = getClient(clientId, subClientId);
        existingClient.setEnabled(clientRequest.isEnabled());
        existingClient.setBatchFileProcessingBundle(clientRequest.getBatchFileProcessingBundle());
        updateClientIrConfig(existingClient, clientRequest.getConfig());
        return save(existingClient);
    }

    public DbClient save(final DbClient client) {
        return clientRepository.save(client);
    }

    public DbClient getClient(final String clientId, final String subClientId) {
        final DbClient client = clientRepository.findByClientIdAndSubClientId(clientId, subClientId);
        if (client == null) {
            final String message =
                    String.format("The client with clientId: '%s' and subClientId: '%s' doesn't exist",
                            clientId,
                            subClientId);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return client;
    }

    private DbIdentityResolutionClientConfig assignIrConfig(
            final DbClient client,
            final PlatformConfig platformConfig) {
        final DbIdentityResolutionClientConfig clientConfig = dbClientConfigMapper.map(platformConfig);
        if (Objects.nonNull(clientConfig)) {
            clientConfig.setClient(client);
            client.setClientConfig(clientConfig);
        }
        return clientConfig;
    }

    private DbIdentityResolutionClientConfig updateClientIrConfig(
            final DbClient dbClient,
            final PlatformConfig config) {
        final DbIdentityResolutionClientConfig existingConfig = dbClient.getClientConfig();
        if (Objects.isNull(existingConfig)) {
            return assignIrConfig(dbClient, config);
        }
        if (Objects.isNull(config)) {
            return existingConfig;
        }
        existingConfig.setMatchLimit(config.getMatchLimit());
        existingConfig.setInsightBatchLimit(config.getInsightsBatchLimit());
        existingConfig.setIdentityBatchLimit(config.getIdentityBatchLimit());
        existingConfig.setAddressThreshold(config.getAddressThreshold());
        existingConfig.setLastNameThreshold(config.getLastNameThreshold());
        existingConfig.setResidentThreshold(config.getResidentThreshold());
        existingConfig.setHouseholdThreshold(config.getHouseholdThreshold());
        existingConfig.setIndividualThreshold(config.getIndividualThreshold());
        existingConfig.setUseGraphId(config.getUseGraphId());
        existingConfig.setEnableKeyring(config.getEnableKeyring());
        existingConfig.setDpvStatusEnabled(config.getDpvStatusEnabled());
        existingConfig.setStdAddressEnabled(config.getStdAddressEnabled());
        existingConfig.setEnableAdminEndpoints(config.getEnableAdminEndpoints());
        existingConfig.setDateOfBirthRangeYears(config.getDateOfBirthRangeYears());
        existingConfig.setEnableFuzzyGenderFilter(config.getEnableFuzzyGenderFilter());
        return existingConfig;
    }
}
