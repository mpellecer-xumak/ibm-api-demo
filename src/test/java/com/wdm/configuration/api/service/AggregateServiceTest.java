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
import com.wdm.configuration.api.persistence.entity.DbClientAggregate;
import com.wdm.configuration.api.persistence.repository.AggregateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AggregateServiceTest {
    private static final String AGGREGATE_NAME = "myAggregate";
    private static final String AGGREGATE_NAME_2 = "myAggregate2";
    private static final String BUNDLE_NAME = "myBundle";
    private static final String BUNDLE_NAME_2 = "myBundle2";
    private static final boolean ENABLED = false;
    @Mock
    private BundlesService bundlesService;
    @Mock
    private AggregateRepository aggregateRepository;
    @Mock
    private DbAggregatesToAggregateMapper dbAggregateAggregateMapper;
    @Mock
    private AggregateToDbAggregate aggregateMapper;
    @InjectMocks
    private AggregateService aggregateService;
    @Captor
    private ArgumentCaptor<DbAggregate> dbAggregateCaptor;

    @Test
    void testCreateAggregateEmptyBundle() {
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, Collections.emptyList());
        final var dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(null);
        when(aggregateMapper.map(aggregate)).thenReturn(dbAggregate);
        when(aggregateRepository.save(dbAggregate)).thenReturn(dbAggregate);
        final var result = aggregateService.createAggregate(aggregate);
        verifyNoInteractions(bundlesService);
        assertEquals(AGGREGATE_NAME, result.getName());
    }

    @Test
    void testCreateAggregateWithBundle() {
        final var bundle = new SimpleName(BUNDLE_NAME);
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, List.of(bundle));
        final var dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(null);
        when(aggregateMapper.map(aggregate)).thenReturn(dbAggregate);
        when(bundlesService.matchBundlesByNames(List.of(BUNDLE_NAME))).thenReturn(Collections.emptyList());
        when(aggregateRepository.save(dbAggregate)).thenReturn(dbAggregate);
        final var result = aggregateService.createAggregate(aggregate);
        assertEquals(AGGREGATE_NAME, result.getName());
    }

    @Test
    void testCreateAggregateExistingAggregate() {
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, Collections.emptyList());
        final var dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(dbAggregate);
        assertThrows(HttpConflictException.class, () -> aggregateService.createAggregate(aggregate));
    }

    @Test
    void testGetAllAggregates() {
        final List<DbAggregate> dbAggregates = getDbAggregates();
        final List<Aggregate<Bundle>> aggregates = getAggregates();

        when(aggregateRepository.findAll()).thenReturn(dbAggregates);
        when(dbAggregateAggregateMapper.map(dbAggregates)).thenReturn(aggregates);

        final List<Aggregate<Bundle>> result = aggregateService.getAllAggregates();
        assertEquals(AGGREGATE_NAME, result.get(0).getName());
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getBundles().size());
        assertEquals(0, result.get(1).getBundles().size());
    }


    @Test
    void testGetAllAggregatesNoAggregatesFound() {
        when(aggregateRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(HttpNotFoundException.class, () -> aggregateService.getAllAggregates());
    }

    @Test
    void testGetDbAggregate() {
        final var aggregate = new DbAggregate();
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(aggregate);
        final DbAggregate result = aggregateService.getDbAggregate(AGGREGATE_NAME);
        assertEquals(aggregate, result);

    }

    @Test
    void testGetDbAggregateNotFound() {
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(null);

        Assertions.assertThrows(HttpNotFoundException.class,
                () -> aggregateService.getDbAggregate(AGGREGATE_NAME));
    }

    @Test
    void testDeleteAggregate() {
        final var aggregate = new DbAggregate();
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(aggregate);
        aggregateService.deleteAggregate(AGGREGATE_NAME);
        verify(aggregateRepository).delete(aggregate);
    }

    @Test
    void testDeleteAggregateNotFound() {
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class,
                () -> aggregateService.deleteAggregate(AGGREGATE_NAME));
        verify(aggregateRepository, never()).delete(any());
    }

    @Test
    void testDeleteAggregateWithClient() {
        final DbAggregate aggregate = new DbAggregate();
        aggregate.setClients(List.of(new DbClientAggregate()));
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(aggregate);
        assertThrows(HttpConflictException.class,
                () -> aggregateService.deleteAggregate(AGGREGATE_NAME));
        verify(aggregateRepository, never()).delete(any());
    }

    @Test
    void testDeleteAggregateWithBundle() {
        final DbAggregate aggregate = new DbAggregate();
        aggregate.setBundles(List.of(new DbBundle()));
        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(aggregate);
        assertThrows(HttpConflictException.class,
                () -> aggregateService.deleteAggregate(AGGREGATE_NAME));
        verify(aggregateRepository, never()).delete(any());
    }

    @Test
    void testUpdateAggregateWithBundle() {
        final var bundle = new SimpleName(BUNDLE_NAME);
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, List.of(bundle));
        final var dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        dbAggregate.setEnabled(Boolean.TRUE);
        final var dbBundle = new DbBundle();
        dbBundle.setName(AGGREGATE_NAME);

        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(dbAggregate);
        when(bundlesService.matchBundlesByNames(List.of(BUNDLE_NAME))).thenReturn(List.of(dbBundle));
        when(aggregateRepository.save(dbAggregate)).thenReturn(dbAggregate);

        final var result = aggregateService.updateAggregate(aggregate);
        verify(aggregateRepository).save(dbAggregateCaptor.capture());
        final var capturedAggregate = dbAggregateCaptor.getValue();

        assertEquals(AGGREGATE_NAME, result.getName());
        assertEquals(1, capturedAggregate.getBundles().size());
        assertFalse(result.isEnabled());
    }

    @Test
    void testUpdateAggregateEmptyBundle() {
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, Collections.emptyList());
        final var dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);

        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(dbAggregate);
        when(aggregateRepository.save(dbAggregate)).thenReturn(dbAggregate);

        final var result = aggregateService.updateAggregate(aggregate);

        verifyNoInteractions(bundlesService);
        verify(aggregateRepository).save(dbAggregateCaptor.capture());
        final var capturedAggregate = dbAggregateCaptor.getValue();

        assertEquals(AGGREGATE_NAME, result.getName());
        assertEquals(0, capturedAggregate.getBundles().size());
    }

    @Test
    void testUpdateAggregateNoExists() {
        final var aggregate = new Aggregate<SimpleName>(AGGREGATE_NAME, ENABLED, Collections.emptyList());

        when(aggregateRepository.findByName(AGGREGATE_NAME)).thenReturn(null);

        assertThrows(HttpNotFoundException.class, () -> aggregateService.updateAggregate(aggregate));
    }

    private List<Aggregate<Bundle>> getAggregates() {
        final Bundle bundle = new Bundle(BUNDLE_NAME);
        final Bundle bundle2 = new Bundle(BUNDLE_NAME_2);
        Aggregate<Bundle> aggregate = new Aggregate<>(AGGREGATE_NAME, ENABLED, List.of(bundle, bundle2));
        Aggregate<Bundle> aggregate2 = new Aggregate<>(AGGREGATE_NAME_2, ENABLED, new ArrayList<>());
        return List.of(aggregate, aggregate2);
    }

    private List<DbAggregate> getDbAggregates() {
        final DbBundle bundle = new DbBundle();
        bundle.setName(BUNDLE_NAME);
        DbAggregate aggregate = new DbAggregate();
        aggregate.setName(AGGREGATE_NAME);
        aggregate.setBundles(List.of(bundle));
        return List.of(aggregate);
    }
}
