package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbAggregatesToAggregateMapper;
import com.wdm.configuration.api.mapper.todbentity.AggregateToDbAggregate;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import com.wdm.configuration.api.persistence.repository.AggregateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class AggregateService {
    private final BundlesService bundlesService;
    private final AggregateRepository aggregateRepository;
    private final AggregateToDbAggregate aggregateMapper;
    private final DbAggregatesToAggregateMapper dbAggregateAggregateMapper;

    public Aggregate<SimpleName> createAggregate(final Aggregate<SimpleName> aggregate) {
        if (aggregateRepository.findByName(aggregate.getName()) != null) {
            final String message = "Duplicated aggregate";
            log.info(message);
            throw new HttpConflictException(message);
        }

        final DbAggregate dbAggregate = aggregateMapper.map(aggregate);
        final List<DbBundle> dbBundles = getDbBundles(aggregate);
        dbAggregate.setBundles(dbBundles);
        aggregateRepository.save(dbAggregate);

        return aggregate;
    }

    public Aggregate<SimpleName> updateAggregate(final Aggregate<SimpleName> aggregate) {
        final String name = aggregate.getName();
        final DbAggregate dbAggregate = aggregateRepository.findByName(name);

        if (dbAggregate == null) {
            final String message = String.format("The aggregate [%s] does not exist", name);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }

        final List<DbBundle> dbBundles = getDbBundles(aggregate);
        dbAggregate.setBundles(dbBundles);
        dbAggregate.setEnabled(aggregate.isEnabled());
        aggregateRepository.save(dbAggregate);

        return aggregate;
    }

    public DbAggregate getDbAggregate(final String name) {
        final DbAggregate dbAggregate = aggregateRepository.findByName(name);
        if (dbAggregate == null) {
            final String message = String.format("Aggregate '%s' does not exist.", name);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return dbAggregate;
    }

    public void deleteAggregate(final String aggregateName) {
        final DbAggregate aggregate = getDbAggregate(aggregateName);
        if (!CollectionUtils.isEmpty(aggregate.getClients())) {
            throw new HttpConflictException(
                    String.format("Cannot delete aggregate '%s' while in use by client", aggregateName));
        }
        if (!CollectionUtils.isEmpty(aggregate.getBundles())) {
            throw new HttpConflictException(
                    String.format("Cannot delete aggregate '%s' while it has bundles", aggregateName));
        }
        aggregateRepository.delete(aggregate);
    }

    @Transactional
    public List<Aggregate<Bundle>> getAllAggregates() {
        List<DbAggregate> dbAggregateList = aggregateRepository.findAll();

        if (CollectionUtils.isEmpty(dbAggregateList)) {
            final String message = "No aggregates were found";
            log.warn(message);
            throw new HttpNotFoundException(message);
        }

        return dbAggregateAggregateMapper.map(dbAggregateList);
    }

    private List<DbBundle> getDbBundles(final Aggregate<SimpleName> aggregate) {
        if (CollectionUtils.isEmpty(aggregate.getBundles())) {
            return Collections.emptyList();
        }
        final List<String> bundleNames = aggregate.getBundles().stream()
                .filter(Objects::nonNull)
                .map(SimpleName::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return bundlesService.matchBundlesByNames(bundleNames);
    }
}
