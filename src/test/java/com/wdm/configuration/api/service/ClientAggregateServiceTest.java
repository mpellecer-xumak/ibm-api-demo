package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpBadRequestException;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.*;
import com.wdm.configuration.api.persistence.entity.*;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.ClientAggregateRepository;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientAggregateServiceTest {
    private final String AGGREGATE_NAME = "myAggregate";
    private final String CLIENT_ID = "myClientId";
    private final String SUB_CLIENT_ID = "mySubClientId";
    private final String BUNDLE_NAME = "myBundle";
    private final String BFP_BUNDLE = "myBundle";

    @Mock
    private AggregateService aggregateService;
    @Mock
    private ClientAggregateRepository clientAggregateRepository;
    @Mock
    private BaseViewRepository<ClientAggregateView, String> aggregateViewRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private BundlesService bundlesService;
    @Mock
    private Mapper<ClientAggregateView, Aggregate<Bundle>> aggregateBundleMapper;
    @Mock
    ClientAggregateView clientAggregateView;

    @InjectMocks
    private ClientAggregateService clientAggregateService;

    @Captor
    private ArgumentCaptor<DbClientAggregate> dbClientAggregateCaptor;

    @Test
    void testAssignAggregateToClient() {
        final DbAggregate dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        final DbClient dbClient = new DbClient();
        dbClient.setClientId(CLIENT_ID);
        dbClient.setSubClientId(SUB_CLIENT_ID);
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        when(aggregateService.getDbAggregate(AGGREGATE_NAME)).thenReturn(dbAggregate);
        Set<ClientAggregateView> aggregatesSet = new HashSet<>();
        clientAggregateView.setAggregateName(AGGREGATE_NAME);
        aggregatesSet.add(clientAggregateView);
        when(clientAggregateRepository.getClientAggregate(
                AGGREGATE_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        when(bundlesService.matchBundlesByNames(anyList())).thenReturn(List.of(dbBundle));
        when(clientAggregateRepository.save(any())).thenReturn(new DbClientAggregate());
        when(aggregateViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(aggregatesSet);
        when(aggregateBundleMapper.map(anySet())).thenReturn(getListAggregates());

        clientAggregateService.assignAggregateToClient(CLIENT_ID, SUB_CLIENT_ID, getAggregate());

        verify(clientAggregateRepository, times(1)).save(dbClientAggregateCaptor.capture());

        final DbClientAggregate savedInstance = dbClientAggregateCaptor.getValue();

        assertEquals(1, savedInstance.getAggregateBundles().size());
        assertEquals(BUNDLE_NAME, savedInstance.getAggregateBundles().get(0).getName());
        assertEquals(AGGREGATE_NAME, savedInstance.getAggregate().getName());

    }

    @Test
    void testAssignAggregateToClient_ExistingClientAggregate() {
        final DbAggregate dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        final DbClient dbClient = new DbClient();
        dbClient.setClientId(CLIENT_ID);
        dbClient.setSubClientId(SUB_CLIENT_ID);
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        Set<ClientAggregateView> aggregatesSet = new HashSet<>();
        clientAggregateView.setAggregateName(AGGREGATE_NAME);
        aggregatesSet.add(clientAggregateView);

        final DbClientAggregate dbClientAggregate = new DbClientAggregate();
        dbClientAggregate.setAggregate(dbAggregate);
        dbClientAggregate.setAggregateBundles(List.of(dbBundle, dbBundle));
        when(aggregateService.getDbAggregate(AGGREGATE_NAME)).thenReturn(dbAggregate);
        when(clientAggregateRepository.getClientAggregate(
                AGGREGATE_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClientAggregate);
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        when(bundlesService.matchBundlesByNames(anyList())).thenReturn(List.of(dbBundle));
        when(clientAggregateRepository.save(any())).thenReturn(new DbClientAggregate());
        when(aggregateViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(aggregatesSet);
        when(aggregateBundleMapper.map(anySet())).thenReturn(getListAggregates());

        clientAggregateService.assignAggregateToClient(CLIENT_ID, SUB_CLIENT_ID, getAggregate());

        verify(clientAggregateRepository, times(1)).save(dbClientAggregateCaptor.capture());

        final DbClientAggregate savedInstance = dbClientAggregateCaptor.getValue();

        assertEquals(1, savedInstance.getAggregateBundles().size());
        assertEquals(BUNDLE_NAME, savedInstance.getAggregateBundles().get(0).getName());
        assertEquals(AGGREGATE_NAME, savedInstance.getAggregate().getName());
    }

    @Test
    void testClientAggregateWithoutBfpBundleThrowsError() {
        final Aggregate<SimpleName> aggregate = getAggregate();
        aggregate.setBatchFileProcessingBundle(null);
        assertThrows(HttpBadRequestException.class, () -> {
            clientAggregateService.assignAggregateToClient(CLIENT_ID, SUB_CLIENT_ID, aggregate);
        });
    }

    @Test
    void testClientAggregateWithBfpBundleEmptyThrowsError() {
        final Aggregate<SimpleName> aggregate = getAggregate();
        aggregate.setBatchFileProcessingBundle(StringUtils.EMPTY);
        assertThrows(HttpBadRequestException.class, () -> {
            clientAggregateService.assignAggregateToClient(CLIENT_ID, SUB_CLIENT_ID, aggregate);
        });
    }

    @Test
    void testClientAggregateWithBfpBundleNotInBundleListThrowsError() {
        final Aggregate<SimpleName> aggregate = getAggregate();
        aggregate.setBatchFileProcessingBundle("Non-existent");
        assertThrows(HttpConflictException.class, () -> {
            clientAggregateService.assignAggregateToClient(CLIENT_ID, SUB_CLIENT_ID, aggregate);
        });
    }

    @Test
    void testRemoveAggregateToClient() {
        final DbAggregate dbAggregate = new DbAggregate();
        dbAggregate.setName(AGGREGATE_NAME);
        final DbClient dbClient = new DbClient();
        dbClient.setClientId(CLIENT_ID);
        dbClient.setSubClientId(SUB_CLIENT_ID);
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        final DbClientAggregate dbClientAggregate = new DbClientAggregate();
        dbClientAggregate.setAggregate(dbAggregate);
        dbClientAggregate.setClient(dbClient);

        when(clientAggregateRepository.getClientAggregate(AGGREGATE_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClientAggregate);
        doNothing().when(clientAggregateRepository).delete(dbClientAggregate);
        when(aggregateViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(Collections.emptySet());
        when(aggregateBundleMapper.map(anySet())).thenReturn(Collections.emptyList());

        clientAggregateService.removeAggregateFromClient(CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME);

        verify(clientAggregateRepository, times(1)).delete(dbClientAggregateCaptor.capture());

        final DbClientAggregate deletedInstance = dbClientAggregateCaptor.getValue();

        assertEquals(AGGREGATE_NAME, deletedInstance.getAggregate().getName());

    }

   @Test
    void testAssignAggregateToClient_NonExistingClientAggregate() {
       when(clientAggregateRepository.getClientAggregate(AGGREGATE_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);

       assertThrows(HttpNotFoundException.class, () -> clientAggregateService.removeAggregateFromClient(CLIENT_ID, SUB_CLIENT_ID, AGGREGATE_NAME));
    }

    private Aggregate<SimpleName> getAggregate() {
        final SimpleName bundle = new SimpleName(BUNDLE_NAME);
        final Aggregate<SimpleName> aggregate = new Aggregate<>();
        aggregate.setName(AGGREGATE_NAME);
        aggregate.setBundles(List.of(bundle));
        aggregate.setBatchFileProcessingBundle(BFP_BUNDLE);
        return aggregate;
    }

    private List<Aggregate<Bundle>> getListAggregates() {
        final List<Aggregate<Bundle>> aggregates = new ArrayList<>();
        final Bundle bundle = new Bundle(BUNDLE_NAME);
        final Aggregate<Bundle> aggregate = new Aggregate<>();
        aggregate.setName(AGGREGATE_NAME);
        aggregate.setBundles(List.of(bundle));
        aggregates.add(aggregate);
        return aggregates;
    }
}
