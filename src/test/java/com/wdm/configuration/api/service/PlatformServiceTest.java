package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbPlatformMatcherToPlatformMatcher;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbPlatformMatcherMapper;
import com.wdm.configuration.api.model.Hierarchy;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.persistence.entity.DbClientMatcher;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import com.wdm.configuration.api.persistence.repository.PlatformMatcherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class PlatformServiceTest {
    private static final String MATCHER_NAME = "myMatcher";
    private static final String HIERARCHY_NAME = "myHierarchy";

    @Mock
    private PlatformMatcherRepository platformMatcherRepository;
    @Mock
    private MatcherService matcherService;
    @Mock
    private HierarchyService hierarchyService;
    @Mock
    private MatcherToDbPlatformMatcherMapper matcherToDbPlatformMatcherMapper;
    @Mock
    private DbPlatformMatcherToPlatformMatcher dbPlatformMatcherToPlatformMatcher;

    @InjectMocks
    private PlatformService platformService;

    @Test
    void testCreatePlatformMatcher() {
        final Matcher matcher = getMatcher();
        final DbPlatformMatcher platformMatcher = new DbPlatformMatcher();
        when(platformMatcherRepository.existsByMatcher_NameAndHierarchy_Name(MATCHER_NAME, HIERARCHY_NAME))
                .thenReturn(false);
        when(matcherService.getDbMatcher(MATCHER_NAME)).thenReturn(new DbMatcher());
        when(hierarchyService.getDbHierarchy(HIERARCHY_NAME)).thenReturn(new DbHierarchy());
        when(matcherToDbPlatformMatcherMapper.map(matcher)).thenReturn(platformMatcher);
        when(platformMatcherRepository.save(platformMatcher)).thenReturn(platformMatcher);

        final Matcher result = platformService.createPlatformMatcher(matcher);

        assertEquals(matcher, result);
    }

    @Test
    void testCreatePlatformMatcher_PlatformMatcherAlreadyExists() {
        final Matcher matcher = getMatcher();
        when(platformMatcherRepository.existsByMatcher_NameAndHierarchy_Name(MATCHER_NAME, HIERARCHY_NAME))
                .thenReturn(true);

        assertThrows(HttpConflictException.class, () -> platformService.createPlatformMatcher(matcher));
    }

    @Test
    void testUpdatePlatformMatcher_WhenPlatformMatcherExists() {
        final Matcher matcher = getMatcher();
        final DbPlatformMatcher dbPlatformMatcher = getDbPlatformMatchers().get(0);
        when(platformMatcherRepository.findDbPlatformMatcherByMatcher_NameAndHierarchy_Name(MATCHER_NAME, HIERARCHY_NAME))
                .thenReturn(Optional.ofNullable(dbPlatformMatcher));
        when(platformMatcherRepository.save(dbPlatformMatcher)).thenReturn(dbPlatformMatcher);

        final Matcher result = platformService.updatePlatformMatcher(matcher);

        assertEquals(matcher, result);
    }

    @Test
    void testUpdatePlatformMatcher_WhenPlatformMatcherNotFound() {
        final Matcher matcher = getMatcher();
        assertThrows(HttpNotFoundException.class, () -> platformService.updatePlatformMatcher(matcher));
    }

    @Test
    void testGetPlatformMatchers() {
        when(platformMatcherRepository.findAll()).thenReturn(getDbPlatformMatchers());
        final List<PlatformMatcher> platformMatchers = platformService.getPlatformMatchers();
        assertNotNull(platformMatchers);
    }

    @Test
    void testDeleteMatcher_Existing(){
        final DbPlatformMatcher dbPlatformMatcher = getDbPlatformMatcher();
        final List<DbPlatformMatcher> dbPlatformMatchers = getDbPlatformMatchers();

        when(platformMatcherRepository.findByNameAndHierarchy(
                MATCHER_NAME, HIERARCHY_NAME)).thenReturn(dbPlatformMatcher);
        when(platformMatcherRepository.findAll()).thenReturn(dbPlatformMatchers);
        when(dbPlatformMatcherToPlatformMatcher.map(dbPlatformMatchers)).thenReturn(getPlatformMatchers());

        List<PlatformMatcher> dbClientMatcher = platformService.deletePlatformMatchers(MATCHER_NAME, HIERARCHY_NAME);

        verify(platformMatcherRepository).delete(dbPlatformMatcher);
        assertEquals(dbClientMatcher.size(), dbPlatformMatchers.size());
    }

    @Test
    void testDeleteMatcher_NotExistent(){
        when(platformMatcherRepository.findByNameAndHierarchy(
                MATCHER_NAME, HIERARCHY_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class,
                () -> platformService.deletePlatformMatchers(MATCHER_NAME, HIERARCHY_NAME));
        verify(platformMatcherRepository, never()).delete(any());
    }

    private List<PlatformMatcher> getPlatformMatchers() {
        final List<PlatformMatcher> dbPlatformMatchers = new ArrayList<>();
        Hierarchy hierarchy = new Hierarchy();
        PlatformMatcher platformMatcher = new PlatformMatcher();
        platformMatcher.setHierarchy(hierarchy.getName());
        platformMatcher.setMatcherDetails(new ArrayList<>());
        dbPlatformMatchers.add(platformMatcher);
        return dbPlatformMatchers;
    }

    private List<DbPlatformMatcher> getDbPlatformMatchers() {
        final List<DbPlatformMatcher> dbPlatformMatchers = new ArrayList<>();
        DbPlatformMatcher dbPlatformMatcher = getDbPlatformMatcher();
        dbPlatformMatchers.add(dbPlatformMatcher);
        return dbPlatformMatchers;
    }

    private DbPlatformMatcher getDbPlatformMatcher() {
        DbHierarchy dbHierarchy = new DbHierarchy();
        DbMatcher dbMatcher = new DbMatcher();
        DbPlatformMatcher dbPlatformMatcher = new DbPlatformMatcher();
        dbPlatformMatcher.setHierarchy(dbHierarchy);
        dbPlatformMatcher.setMatcher(dbMatcher);
        return dbPlatformMatcher;
    }

    private Matcher getMatcher() {
        final Matcher matcher = new Matcher();
        matcher.setName(MATCHER_NAME);
        matcher.setHierarchy(HIERARCHY_NAME);
        return matcher;
    }
}
