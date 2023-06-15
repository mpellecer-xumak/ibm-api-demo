package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbAggregateBundleToBundleMapper;
import com.wdm.configuration.api.mapper.fromdbentity.DbBundleToBundleMapper;
import com.wdm.configuration.api.model.*;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.repository.BundleRepository;
import com.wdm.configuration.api.persistence.repository.ClientAggregateViewRepository;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BundlesServiceTest {

    private final String CLIENT_ID = "client_id";
    private final String SUB_CLIENT_ID = "sub_client_id";
    private final String REMOVE_CLIENT_ID = "C-001";
    private final String REMOVE_SUB_CLIENT_ID = "SC-001";

    private final String BUNDLE_NAME = "bundle_name";
    private final String BUNDLE_NAME_2 = "bundle_name_2";
    private static final String ATTRIBUTE_NAME = "myAttribute";
    private static final String ATTRIBUTE_NAME_2 = "myAttribute2";

    @Mock
    private DbBundleToBundleMapper bundlesMapper;

    @Mock
    private DbAggregateBundleToBundleMapper aggregateMapper;

    @Mock
    private ClientAggregateViewRepository aggregateRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private BundleRepository bundleRepository;

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private BundlesService bundlesService;

    private DbBundle dbBundle;
    private DbClient client;
    private Bundle bundle;

    @BeforeEach
    void setup() {
        dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        bundle = new Bundle();
        bundle.setName(BUNDLE_NAME);
        client = mock(DbClient.class);
        when(client.getClientId()).thenReturn(CLIENT_ID);
        when(client.getSubClientId()).thenReturn(SUB_CLIENT_ID);
        when(client.getHumanInsightBundles()).thenReturn(List.of(dbBundle));
        when(aggregateRepository.findAll(anyString(), anyString())).thenReturn(Collections.emptySet());
    }

    @Test
    void getBundleInformation() {
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setId(1);
        dbBundle.setName(BUNDLE_NAME);

        when(bundleRepository.findByName(BUNDLE_NAME)).thenReturn(dbBundle);
        when(bundlesMapper.map(any(DbBundle.class))).thenReturn(bundle);

        final Bundle resultBundle = bundlesService.getBundle(BUNDLE_NAME);

        assertNotNull(resultBundle);
    }

    @Test
    void getBundleInformation_NotFound() {
        when(bundleRepository.findByName(BUNDLE_NAME)).thenReturn(null);

        final Throwable exception = assertThrows(HttpNotFoundException.class, ()-> bundlesService.getBundle(BUNDLE_NAME));

        assertEquals("404 No bundle was found", exception.getMessage());
    }

    @Test
    void testGetBundlesClientId() {
        when(clientService.getClient(CLIENT_ID, null)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));

        final Set<Bundle> result = bundlesService.getBundles(CLIENT_ID);
        final Bundle resultBundle = result.iterator().next();
        assertNotNull(resultBundle.getBundleType());
        assertEquals(dbBundle.getName(), resultBundle.getName());
    }

    @Test
    void testGetBundlesClientIdAndSubClientId() {
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));

        final Set<Bundle> result = bundlesService.getBundles(CLIENT_ID, SUB_CLIENT_ID);
        final Bundle resultBundle = result.iterator().next();
        assertNotNull(resultBundle.getBundleType());
        assertEquals(dbBundle.getName(), resultBundle.getName());
    }

    @Test
    void testGetBundlesClientIdAndSubClientId_NoClient() {
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);

        final Set<Bundle> result = bundlesService.getBundles(CLIENT_ID, SUB_CLIENT_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBundleClientId() {
        when(clientService.getClient(CLIENT_ID, null)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));

        final Bundle result = bundlesService.getBundle(CLIENT_ID, BUNDLE_NAME);
        assertEquals(BUNDLE_NAME, result.getName());
    }

    @Test
    void testGetBundleClientIdAndSubClientId() {
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));

        final Bundle result = bundlesService.getBundle(CLIENT_ID, SUB_CLIENT_ID, BUNDLE_NAME);
        assertEquals(BUNDLE_NAME, result.getName());
    }

    @Test
    void testGetBundleClientIdAndSubClientId_NoClient() {
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);

        final Bundle result = bundlesService.getBundle(CLIENT_ID, SUB_CLIENT_ID, BUNDLE_NAME);
        assertNull(result);
    }

    @Test
    void testGetBundleClientIdAndSubClientId_NoBundle() {
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(client);

        final Bundle result = bundlesService.getBundle(CLIENT_ID, SUB_CLIENT_ID, BUNDLE_NAME_2);
        assertNull(result);
    }

    @Test
    void testReturnBothBundleTypes() {
        final ClientAggregateView view = new ClientAggregateView();
        final Bundle bundle2 = new Bundle();
        bundle2.setName(BUNDLE_NAME_2);
        when(clientService.getClient(CLIENT_ID, null)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));
        when(aggregateRepository.findAll(anyString(), anyString())).thenReturn(Set.of(view));
        when(aggregateMapper.map(anySet())).thenReturn(List.of(bundle2));

        final Set<Bundle> result = bundlesService.getBundles(CLIENT_ID);
        final boolean hasHumanInsightBundle =
                result.stream().anyMatch(bundle -> BundleType.HUMAN_INSIGHT.equals(bundle.getBundleType()));
        final boolean hasAggregateBundle =
                result.stream().anyMatch(bundle -> BundleType.AGGREGATE.equals(bundle.getBundleType()));

        assertTrue(hasHumanInsightBundle);
        assertTrue(hasAggregateBundle);
    }

    @Test
    void testCreateBundle() {
        when(bundleRepository.existsByName(BUNDLE_NAME)).thenReturn(Boolean.FALSE);
        when(attributeService.createAttributes(anyList())).thenReturn(Collections.emptyList());
        when(bundleRepository.save(any(DbBundle.class))).thenReturn(dbBundle);
        when(bundlesMapper.map(dbBundle)).thenReturn(bundle);

        final var result = bundlesService.createBundle(bundle);

        assertEquals(BUNDLE_NAME, result.getName());
        assertNull(result.getAttributes());
    }

    @Test
    void testCreateBundle_AlreadyExists() {
        when(bundleRepository.existsByName(BUNDLE_NAME)).thenReturn(Boolean.TRUE);

        final var result = bundlesService.createBundle(bundle);

        assertNull(result);
    }

    @Test
    void testAssignHumanInsightsBundles() {
        final var dbClient = new DbClient();
        dbClient.setHumanInsightBundles(new ArrayList<>());
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        when(bundleRepository.findByNameIn(anyList())).thenReturn(getDbBundles());
        when(clientService.save(dbClient)).thenReturn(dbClient);
        when(bundlesMapper.map(anyList())).thenReturn(Collections.emptyList());
        final var result = bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID, getBundles());
        assertNotNull(result);
    }

    @Test
    void testAssignHumanInsightsBundles_EmptyBundles() {
        assertThrows(HttpConflictException.class,
                () -> bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID,
                        Collections.emptyList()));
    }

    @Test
    void testAssignHumanInsightsBundles_AllBundlesAlreadyAssociated() {
        final var dbClient = new DbClient();
        dbClient.setHumanInsightBundles(getDbBundles());
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        assertThrows(HttpConflictException.class,
                () -> bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID, getBundles()));

        verify(clientService, times(0)).save(any());
        verifyNoInteractions(bundlesMapper);
    }

    @Test
    void testAssignHumanInsightsBundles_BundlesDontExistInTheDB() {
        final var dbClient = new DbClient();
        dbClient.setHumanInsightBundles(Collections.emptyList());
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        when(bundleRepository.findByNameIn(anyList())).thenReturn(Collections.emptyList());
        assertThrows(HttpConflictException.class,
                () -> bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID, getBundles()));

        verify(clientService, times(0)).save(any());
        verifyNoInteractions(bundlesMapper);
    }

    @Test
    void testAssignHumanInsightsBundles_OneBundleDontExistInTheDB() {
        final var dbClient = new DbClient();
        dbClient.setHumanInsightBundles(Collections.emptyList());
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClient);
        when(bundleRepository.findByNameIn(anyList())).thenReturn(getDbBundles());
        final var bundle1 = new SimpleName(BUNDLE_NAME);
        final var bundle2 = new SimpleName(BUNDLE_NAME_2);
        assertThrows(HttpConflictException.class,
                () -> bundlesService.assignHumanInsightBundlesToClient(CLIENT_ID, SUB_CLIENT_ID,
                        List.of(bundle1, bundle2)));

        verify(clientService, times(0)).save(any());
        verifyNoInteractions(bundlesMapper);
    }

    @Test
    void testRemoveAttributeBundle() {
        final DbBundle dbBundle = getDbBundleWithAttributesBD(2);

        final DbBundle dbBundleDelete = getDbBundleWithAttributesBD(1);
        final Bundle bundle = getDbBundleWithOneAttribute();

        when(bundleRepository.findByName(BUNDLE_NAME)).thenReturn(dbBundle);
        when(bundleRepository.save(dbBundle)).thenReturn(dbBundleDelete);
        when(bundlesMapper.map(dbBundleDelete)).thenReturn(bundle);

        final Bundle bundleResponse = bundlesService.removeAttributeBundle(BUNDLE_NAME, ATTRIBUTE_NAME_2);
        assertEquals(BUNDLE_NAME, bundleResponse.getName());
        assertEquals(bundleResponse.getAttributes().size(), 1);
    }

    @Test
    void testRemoveAttributeBundle_BundleDoesNotExist() {
        when(bundleRepository.findByName(BUNDLE_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> bundlesService.removeAttributeBundle(BUNDLE_NAME, ATTRIBUTE_NAME_2));
    }

    @Test
    void testRemoveAttributeBundle_AtributeDoesNotExist() {
        final DbBundle dbBundle = getDbBundleWithAttributesBD(1);

        when(bundleRepository.findByName(BUNDLE_NAME)).thenReturn(dbBundle);
        assertThrows(HttpNotFoundException.class, () -> bundlesService.removeAttributeBundle(BUNDLE_NAME, ATTRIBUTE_NAME_2));
    }

    @Test
    void testGetBundleNames() {
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setId(1);
        dbBundle.setName("testBundle");

        when(bundleRepository.findAll()).thenReturn(List.of(dbBundle));
        when(bundlesMapper.map(anyList())).thenReturn(List.of(bundle));

        final List<Bundle> bundleList = bundlesService.getBundleNames();

        assertNotNull(bundleList);
    }

    @Test
    void testGetBundleNames_NotFound() {
        when(bundleRepository.findAll()).thenReturn(Collections.emptyList());

        final Throwable exception = assertThrows(HttpNotFoundException.class, ()-> bundlesService.getBundleNames());

        assertEquals("404 No bundles were found", exception.getMessage());
    }

    @Test
    void removeBundle_ExpectSuccess() {
        final List<String> bundleNames = List.of(BUNDLE_NAME, BUNDLE_NAME_2);

        final List<DbBundle> dbBundleList = bundleNames.stream()
                .map(n -> {
                    final var dbBundle = new DbBundle();
                    dbBundle.setName(n);
                    return dbBundle;
                })
                .collect(Collectors.toList());

        final List<Bundle> bundleList = bundleNames.stream()
                .map(Bundle::new)
                .collect(Collectors.toList());

        final List<SimpleName> simpleNameList = bundleNames.stream()
                .map(SimpleName::new)
                .collect(Collectors.toList());

        final var client = mock(DbClient.class);
        when(client.getClientId()).thenReturn(REMOVE_CLIENT_ID);
        when(client.getSubClientId()).thenReturn(REMOVE_SUB_CLIENT_ID);
        when(client.getHumanInsightBundles()).thenReturn(dbBundleList);

        when(clientService.getClient(REMOVE_CLIENT_ID, REMOVE_SUB_CLIENT_ID)).thenReturn(client);
        when(bundlesMapper.map(anyList())).thenReturn(bundleList);

        final var response = bundlesService.removeClientBundles(REMOVE_CLIENT_ID, REMOVE_SUB_CLIENT_ID, simpleNameList);
        assertEquals(bundleNames.size(), response.size());
        assertTrue(response.stream()
                .allMatch(b -> bundleNames.contains(b.getName())));

    }

    @Test
    void removeBundle_ExpectException() {
        final List<String> bundleNames = List.of(BUNDLE_NAME, BUNDLE_NAME_2);

        final List<DbBundle> dbBundleList = bundleNames.stream()
                .map(n -> {
                    final var dbBundle = new DbBundle();
                    dbBundle.setName(n);
                    return dbBundle;
                })
                .limit(1)
                .collect(Collectors.toList());

        final List<SimpleName> simpleNameList = bundleNames.stream()
                .map(SimpleName::new)
                .collect(Collectors.toList());

        final var client = mock(DbClient.class);
        when(client.getClientId()).thenReturn(REMOVE_CLIENT_ID);
        when(client.getSubClientId()).thenReturn(REMOVE_SUB_CLIENT_ID);
        when(client.getHumanInsightBundles()).thenReturn(dbBundleList);

        when(clientService.getClient(REMOVE_CLIENT_ID, REMOVE_SUB_CLIENT_ID)).thenReturn(client);

        final var exception = assertThrows(HttpNotFoundException.class,
                () ->
                        bundlesService.removeClientBundles(REMOVE_CLIENT_ID, REMOVE_SUB_CLIENT_ID, simpleNameList)
        );

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(BUNDLE_NAME_2));
    }

    static Stream<Arguments> getInvalidBundleNameList() {
        return Stream.of(
                Arguments.of(new ArrayList<SimpleName>()),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidBundleNameList")
    void removeBundle_EmptyOrNullBundleListGiven_ExpectException(final List<SimpleName> list) {
        assertThrows(HttpConflictException.class,
                () ->
                        bundlesService.removeClientBundles(REMOVE_CLIENT_ID, REMOVE_SUB_CLIENT_ID, list)
        );
    }

    @Test
    void testDeleteBundle() {
        when(bundleRepository.findFirstByName(BUNDLE_NAME)).thenReturn(dbBundle);
        bundlesService.deleteBundle(BUNDLE_NAME);
        verify(bundleRepository).delete(dbBundle);
    }

    @Test
    void testDeleteBundleNotFound() {
        when(bundleRepository.findFirstByName(BUNDLE_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> bundlesService.deleteBundle(BUNDLE_NAME));
        verify(bundleRepository, never()).delete(dbBundle);
    }

    @Test
    void testDeleteBundleWithAggregate() {
        when(bundleRepository.findFirstByName(BUNDLE_NAME)).thenReturn(dbBundle);
        dbBundle.setAggregateBundles(List.of(new DbAggregate()));
        assertThrows(HttpConflictException.class, () -> bundlesService.deleteBundle(BUNDLE_NAME));
        verify(bundleRepository, never()).delete(dbBundle);
    }

    @Test
    void testDeleteBundleWithClient() {
        when(bundleRepository.findFirstByName(BUNDLE_NAME)).thenReturn(dbBundle);
        dbBundle.setHumanInsightClients(List.of(new DbClient()));
        assertThrows(HttpConflictException.class, () -> bundlesService.deleteBundle(BUNDLE_NAME));
        verify(bundleRepository, never()).delete(dbBundle);
    }

    private List<SimpleName> getBundles() {
        final var bundle = new SimpleName(BUNDLE_NAME);
        return List.of(bundle);
    }

    private List<DbBundle> getDbBundles() {
        final var dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        return List.of(dbBundle);
    }

    private DbBundle getDbBundleWithAttributesBD(final int count) {
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        final List<DbAttribute> attributes = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            attributes.add(new DbAttribute(ATTRIBUTE_NAME + i));
        }
        dbBundle.setAttributes(attributes);
        return dbBundle;
    }

    private Bundle getDbBundleWithOneAttribute() {
        final Bundle dbBundle = new Bundle();
        dbBundle.setName(BUNDLE_NAME);
        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(ATTRIBUTE_NAME));
        dbBundle.setAttributes(attributes);
        return dbBundle;
    }
}
