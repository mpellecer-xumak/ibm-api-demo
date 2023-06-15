package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbSuppressionViewToSuppressionMapper;
import com.wdm.configuration.api.mapper.todbentity.SuppressionToDbClientSuppression;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientSuppression;
import com.wdm.configuration.api.persistence.entity.DbSuppression;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.ClientSuppressionRepository;
import com.wdm.configuration.api.persistence.view.ClientSuppressionView;
import com.wdm.configuration.api.persistence.view.IdentityResolutionClientConfigView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientSuppressionService {
    private final ClientService clientService;
    private final SuppressionsService suppressionsService;
    private final ClientSuppressionRepository clientSuppressionRepository;
    private final SuppressionToDbClientSuppression toDbClientSuppression;
    private final DbSuppressionViewToSuppressionMapper suppressionMapper;
    private final BaseViewRepository<ClientSuppressionView, String> suppressionViewRepository;
    private final BaseViewRepository<IdentityResolutionClientConfigView, String> clientConfigViewRepository;

    public Suppression associateSuppression(final String clientId, final String subClientId, final Suppression suppression) {
        final boolean clientSuppressionExists =
                clientSuppressionRepository.existsClientSuppression(suppression.getName(), clientId, subClientId);
        if (clientSuppressionExists) {
            final String message = "The suppression is already associated to the client";
            log.warn(message, suppression.getName());
            throw new HttpConflictException(message);
        }
        final DbClient dbClient = clientService.getClient(clientId, subClientId);
        final DbSuppression dbSuppression = suppressionsService.getDbSuppression(suppression.getName());
        final DbClientSuppression dbClientSuppression = toDbClientSuppression.map(suppression);
        dbClientSuppression.setClient(dbClient);
        dbClientSuppression.setSuppression(dbSuppression);
        clientSuppressionRepository.save(dbClientSuppression);
        return suppression;
    }

    public List<Suppression> getSuppressionClientConfig(final String clientId, final String subClientId) {
        final Set<ClientSuppressionView> suppressionViews = suppressionViewRepository.findAll(clientId, subClientId);
        if (CollectionUtils.isEmpty(suppressionViews)) {
            throw new HttpNotFoundException("Client does not have suppression associated");
        }
        return suppressionMapper.map(suppressionViews);
    }

    public List<Suppression> updateClientSuppresion(final Suppression suppression, final String clientId, final String subClientId){
        DbClientSuppression clientSuppression = clientSuppressionRepository.getClientSuppression(suppression.getName(), clientId, subClientId);

        if (clientSuppression == null) {
            final String message = "The suppression is not associated to the client";
            log.warn(message, suppression.getName());
            throw new HttpConflictException(message);
        }

        final DbClient dbClient = clientSuppression.getClient();
        final DbSuppression dbSuppression = clientSuppression.getSuppression();
        final int id = clientSuppression.getId();

        clientSuppression = toDbClientSuppression.map(suppression);
        clientSuppression.setId(id);
        clientSuppression.setClient(dbClient);
        clientSuppression.setSuppression(dbSuppression);
        clientSuppressionRepository.save(clientSuppression);

        return this.getSuppressionClientConfig(clientId, subClientId);
    }

    public List<Suppression> deleteClientSuppression(final String suppressionName, final String clientId, final String subClientId){
        DbClientSuppression clientSuppression = clientSuppressionRepository.getClientSuppression(suppressionName, clientId, subClientId);
        if (clientSuppression == null) {
            final String message = "The suppression is not associated to the client";
            log.warn(message, suppressionName);
            throw new HttpNotFoundException(message);
        }
        clientSuppressionRepository.delete(clientSuppression);
        return this.getSuppressionClientConfig(clientId, subClientId);
    }
}
