package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbMatcherToMatcherMapper;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbMatcherMapper;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbClientMatcher;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import com.wdm.configuration.api.persistence.repository.MatcherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatcherServiceTest {
    private static final String MATCHER_NAME = "my-matcher";

    @Mock
    private MatcherRepository matcherRepository;
    @Mock
    private MatcherToDbMatcherMapper toDbMatcherMapper;
    @Mock
    private DbMatcherToMatcherMapper toMatcherMapper;

    @InjectMocks
    private MatcherService matcherService;

    @Test
    void testCreateMatchers() {
        final SimpleName matcherName = new SimpleName(MATCHER_NAME);
        final List<SimpleName> matcherNames = List.of(matcherName);
        final DbMatcher dbMatcher = new DbMatcher();
        dbMatcher.setName(MATCHER_NAME);
        final List<DbMatcher> dbMatchers = List.of(dbMatcher);
        when(matcherRepository.findAllByNameIn(anyList())).thenReturn(Collections.emptyList());
        when(toDbMatcherMapper.map(matcherNames)).thenReturn(dbMatchers);
        when(matcherRepository.saveAll(dbMatchers)).thenReturn(dbMatchers);
        when(toMatcherMapper.map(dbMatchers)).thenReturn(matcherNames);

        final List<SimpleName> result = matcherService.createMatchers(matcherNames);

        assertEquals(matcherNames, result);
    }

    @Test
    void testCreateMatchers_MatcherExists() {
        final SimpleName matcher = new SimpleName(MATCHER_NAME);
        final List<SimpleName> matchers = List.of(matcher);
        final DbMatcher dbMatcher = new DbMatcher();
        dbMatcher.setName(MATCHER_NAME);
        final List<DbMatcher> dbMatchers = List.of(dbMatcher);
        when(matcherRepository.findAllByNameIn(anyList())).thenReturn(dbMatchers);

        assertThrows(HttpConflictException.class, () -> matcherService.createMatchers(matchers));
    }

    @Test
    void testGetDbMatcher() {
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(new DbMatcher());
        final DbMatcher dbMatcher = matcherService.getDbMatcher(MATCHER_NAME);
        assertNotNull(dbMatcher);
    }

    @Test
    void testGetDbMatcher_NotFound() {
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> matcherService.getDbMatcher(MATCHER_NAME));
    }

    @Test
    void testDeleteMatcher() {
        final DbMatcher dbMatcher= new DbMatcher();
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(dbMatcher);
        matcherService.deleteMatcher(MATCHER_NAME);
        verify(matcherRepository).delete(dbMatcher);
    }

    @Test
    void testDeleteMatcherNotFound() {
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> matcherService.deleteMatcher(MATCHER_NAME));
        verify(matcherRepository, never()).delete(any());
    }

    @Test
    void testDeleteMatcherWithClient() {
        final DbMatcher dbMatcher= new DbMatcher();
        dbMatcher.setClientMatchers(List.of(new DbClientMatcher()));
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(dbMatcher);
        assertThrows(HttpConflictException.class, () -> matcherService.deleteMatcher(MATCHER_NAME));
        verify(matcherRepository, never()).delete(any());
    }

    @Test
    void testDeleteMatcherWithPlatform() {
        final DbMatcher dbMatcher= new DbMatcher();
        dbMatcher.setPlatformMatchers(List.of(new DbPlatformMatcher()));
        when(matcherRepository.findByName(MATCHER_NAME)).thenReturn(dbMatcher);
        assertThrows(HttpConflictException.class, () -> matcherService.deleteMatcher(MATCHER_NAME));
        verify(matcherRepository, never()).delete(any());
    }
}
