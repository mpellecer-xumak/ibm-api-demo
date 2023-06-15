package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.wdm.configuration.api.mapper.fromdbentity.DbMatcherViewToMatcherMapper;
import com.wdm.configuration.api.model.HierarchyEnum;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbClientMatcherMapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.entity.*;
import com.wdm.configuration.api.persistence.repository.ClientMatcherRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ClientMatcherServiceTest {
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String MATCHER_NAME = "myMatcher";
    private static final String HIERARCHY_NAME = "myHierarchy";
    private static final Integer INDEX = 1;
    private static final Integer MODIFIED_INDEX = 2;
    private static final Integer MATCH_LEVEL = 11;
    private static final Integer MODIFIED_MATCH_LEVEL = 22;

    @Mock
    private ClientService clientService;
    @Mock
    private MatcherService matcherService;
    @Mock
    private HierarchyService hierarchyService;
    @Mock
    private ClientMatcherRepository clientMatcherRepository;
    @Mock
    private MatcherToDbClientMatcherMapper toDbClientMatcherMapper;
    @Mock
    private BaseViewRepository<ClientMatcherView, String> matcherViewRepository;
    @Mock
    private DbMatcherViewToMatcherMapper matcherMapper;

    @InjectMocks
    @Spy
    private ClientMatcherService clientMatcherService;

    @Captor
    private ArgumentCaptor<DbClientMatcher> clientMatcherCaptor;

    @Test
    void testAssociateMatcher() {
        final Matcher matcher = getMatcher();
        final DbMatcher dbMatcher = new DbMatcher();
        dbMatcher.setName(MATCHER_NAME);
        final DbHierarchy dbHierarchy = new DbHierarchy();
        dbHierarchy.setName(HIERARCHY_NAME);
        when(clientMatcherRepository.doesClientMatcherExists(
                        CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(false);
        when(toDbClientMatcherMapper.map(matcher)).thenReturn(new DbClientMatcher());
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(new DbClient());
        when(matcherService.getDbMatcher(MATCHER_NAME)).thenReturn(dbMatcher);
        when(hierarchyService.getDbHierarchy(HIERARCHY_NAME)).thenReturn(dbHierarchy);
        when(clientMatcherRepository.save(any())).thenReturn(new DbClientMatcher());
        final Matcher result = clientMatcherService.associateMatcher(CLIENT_ID, SUB_CLIENT_ID, matcher);
        verify(clientMatcherRepository).save(clientMatcherCaptor.capture());
        final DbClientMatcher savedDbClientMatcher = clientMatcherCaptor.getValue();

        assertEquals(MATCHER_NAME, result.getName());
        assertEquals(HIERARCHY_NAME, result.getHierarchy());
        assertEquals(dbMatcher, savedDbClientMatcher.getMatcher());
        assertEquals(dbHierarchy, savedDbClientMatcher.getHierarchy());
    }

    @Test
    void testAssociateMatcher_ClientMatcherExists() {
        final Matcher matcher = getMatcher();
        when(clientMatcherRepository .doesClientMatcherExists(
                        CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(true);
        assertThrows(HttpConflictException.class,
                () -> clientMatcherService.associateMatcher(CLIENT_ID, SUB_CLIENT_ID, matcher));
        verifyNoInteractions(clientService, toDbClientMatcherMapper, matcherService, hierarchyService);
    }

    @Test
    void testUpdateClientMatcher_ExistingCM() {
        when(clientMatcherRepository.findClientMatcher(
                    CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(getDbClientMatcher());
        when(clientMatcherRepository.save(any(DbClientMatcher.class))).then(a -> a.getArguments()[0]);
        final Matcher result = clientMatcherService.updateMatcher(CLIENT_ID, SUB_CLIENT_ID, getMatcher());
        verify(clientMatcherRepository).save(clientMatcherCaptor.capture());
        final DbClientMatcher modifiedClientMatcher = clientMatcherCaptor.getValue();

        assertEquals(MODIFIED_INDEX, modifiedClientMatcher.getIndex());
        assertEquals(MODIFIED_MATCH_LEVEL, modifiedClientMatcher.getMatchLevel());
        
        assertEquals(MODIFIED_INDEX, result.getIndex());
        assertEquals(MODIFIED_MATCH_LEVEL, result.getMatchLevel());

    }

    @Test
    void testUpdateClientMatcher_NonExistentCM() {
        final Matcher matcher = getMatcher();
        when(clientMatcherRepository.findClientMatcher(
                    CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> 
                clientMatcherService.updateMatcher(CLIENT_ID, SUB_CLIENT_ID, matcher));
    }

    @Test
    void testDeleteClientMatcher_Existing(){
        final DbClientMatcher dbClientMatcher = getDbClientMatcher();
        final Set<ClientMatcherView> clientMatcherViews = clientMatcherView();
        final List<MatcherHierarchyGroup> matcherHierarchyGroups1 = matcherHierarchyGroups();
        when(clientMatcherRepository.findClientMatcher(
                CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(dbClientMatcher);
        when(matcherViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(clientMatcherViews);
        when(matcherMapper.map(clientMatcherViews)).thenReturn(matcherHierarchyGroups1);

        final ClientMachersDeleteRequest deleteRequest = getClientMachersDeleteRequest();
        List<MatcherHierarchyGroup> matcherHierarchyGroups = clientMatcherService.deleteClientMatcher(CLIENT_ID, SUB_CLIENT_ID, deleteRequest);

        verify(clientMatcherRepository).delete(dbClientMatcher);
        assertEquals(matcherHierarchyGroups.size(), matcherHierarchyGroups().size());
    }

    @Test
    void testDeleteClientMatcher_NotExistent(){
        when(clientMatcherRepository.findClientMatcher(
                CLIENT_ID, SUB_CLIENT_ID, MATCHER_NAME, HIERARCHY_NAME)).thenReturn(null);
        final ClientMachersDeleteRequest deleteRequest = getClientMachersDeleteRequest();
        assertThrows(HttpNotFoundException.class,
                () -> clientMatcherService.deleteClientMatcher(CLIENT_ID, SUB_CLIENT_ID, deleteRequest));
        verify(clientMatcherRepository, never()).delete(any());
    }

    private DbClientMatcher getDbClientMatcher() {
        final DbClientMatcher dbClientMatcher = new DbClientMatcher();
        dbClientMatcher.setIndex(INDEX);
        dbClientMatcher.setMatchLevel(MATCH_LEVEL);
        return dbClientMatcher;
    }

    private Matcher getMatcher() {
        final Matcher matcher = new Matcher();
        matcher.setName(MATCHER_NAME);
        matcher.setHierarchy(HIERARCHY_NAME);
        matcher.setIndex(MODIFIED_INDEX);
        matcher.setMatchLevel(MODIFIED_MATCH_LEVEL);
        return matcher;
    }

    private Set<ClientMatcherView> clientMatcherView(){
        final ClientMatcherView clientMatcherView = new ClientMatcherView();
        clientMatcherView.setMatcherName(MATCHER_NAME);
        clientMatcherView.setHierarchyName(HIERARCHY_NAME);
        return Set.of(clientMatcherView);
    }

    private List<MatcherHierarchyGroup> matcherHierarchyGroups(){
        final MatcherHierarchyGroup hierarchyGroup = new MatcherHierarchyGroup();
        hierarchyGroup.setHierarchy(HierarchyEnum.IDENTITY);
        hierarchyGroup.setMatcherDetails(List.of(getMatcher()));
        return List.of(hierarchyGroup);
    }

    private ClientMachersDeleteRequest getClientMachersDeleteRequest() {
        final ClientMachersDeleteRequest deleteRequest = new ClientMachersDeleteRequest();
        deleteRequest.setMatcherName(MATCHER_NAME);
        deleteRequest.setHierarchyName(HIERARCHY_NAME);
        return deleteRequest;
    }
}
