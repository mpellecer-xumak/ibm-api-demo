package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpBadRequestException;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientAggregate;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.ClientAggregateRepository;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClientAggregateService {
    private final AggregateService aggregateService;
    private final ClientAggregateRepository clientAggregateRepository;
    private final BaseViewRepository<ClientAggregateView, String> aggregateViewRepository;

    private final ClientService clientService;
    private final BundlesService bundlesService;

    private final Mapper<ClientAggregateView, Aggregate<Bundle>> aggregateBundleMapper;

    public List<Aggregate<Bundle>> assignAggregateToClient(final String clientId, final String subClientId,
                                                           final Aggregate<SimpleName> aggregate) {
        checkBundleName(aggregate.getBatchFileProcessingBundle(), aggregate.getBundles());
        final DbClientAggregate dbClientAggregate = Optional.ofNullable(
                        clientAggregateRepository.getClientAggregate(
                                aggregate.getName(), clientId, subClientId))
                .orElse(new DbClientAggregate());

        if (dbClientAggregate.getClient() == null) {
            log.info("Aggregate is not associated, associating...");
            final DbClient client = clientService.getClient(clientId, subClientId);
            final DbAggregate dbAggregate = aggregateService.getDbAggregate(aggregate.getName());
            dbClientAggregate.setClient(client);
            dbClientAggregate.setAggregate(dbAggregate);
        }
        dbClientAggregate.setAggregateBundles(getDbBundles(aggregate.getBundles()));
        dbClientAggregate.setBatchFileProcessingBundle(aggregate.getBatchFileProcessingBundle());
        dbClientAggregate.setEnabled(aggregate.isEnabled());
        clientAggregateRepository.save(dbClientAggregate);
        return getClientAggregates(clientId, subClientId);
    }

    public List<Aggregate<Bundle>> removeAggregateFromClient(final String clientId, final String subClientId,
                                                             final String aggregateName) {
        final DbClientAggregate dbClientAggregate = clientAggregateRepository.getClientAggregate(aggregateName, clientId, subClientId);
        if (dbClientAggregate == null) {
            throw new HttpNotFoundException("Client does not have the aggregate associated");
        }
        clientAggregateRepository.delete(dbClientAggregate);
        return aggregateBundleMapper.map(aggregateViewRepository.findAll(clientId, subClientId));
    }

    public List<Aggregate<Bundle>> getClientAggregates(final String clientId, final String subClientId) {
        final Set<ClientAggregateView> aggregateViews = aggregateViewRepository.findAll(clientId, subClientId);
        if (CollectionUtils.isEmpty(aggregateViews)) {
            throw new HttpNotFoundException("Client does not have aggregates associated");
        }
        return aggregateBundleMapper.map(aggregateViews);
    }

    private void checkBundleName(final String bundleName, final List<SimpleName> simpleNames) {
        if (StringUtils.isBlank(bundleName)) {
            throw new HttpBadRequestException("The batch file processing bundle is required");
        }
        boolean hasBundle = simpleNames.stream().map(SimpleName::getName).anyMatch(name -> name.equals(bundleName));
        if (!hasBundle) {
            throw new HttpConflictException("The batch file processing bundle is missing in the list of bundles");
        }
    }

    private List<DbBundle> getDbBundles(final List<SimpleName> simpleNames) {
        if (CollectionUtils.isEmpty(simpleNames)) {
            return Collections.emptyList();
        }
        final List<String> bundleNames = simpleNames.stream()
                .map(SimpleName::getName)
                .collect(Collectors.toList());
        final List<DbBundle> dbBundles = bundlesService.matchBundlesByNames(bundleNames);
        return dbBundles;
    }

}
