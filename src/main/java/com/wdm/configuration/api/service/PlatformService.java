package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbPlatformMatcherToPlatformMatcher;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbPlatformMatcherMapper;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import com.wdm.configuration.api.persistence.repository.PlatformMatcherRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class PlatformService {
    private final PlatformMatcherRepository platformMatcherRepository;
    private final MatcherService matcherService;
    private final HierarchyService hierarchyService;
    private final MatcherToDbPlatformMatcherMapper matcherToDbPlatformMatcherMapper;
    private final DbPlatformMatcherToPlatformMatcher dbPlatformMatcherToPlatformMatcher;

    public Matcher createPlatformMatcher(final Matcher matcher) {
        final String matcherName = matcher.getName();
        final String hierarchyName = matcher.getHierarchy();
        final boolean pmAlreadyExist = platformMatcherRepository.existsByMatcher_NameAndHierarchy_Name(
                matcherName, hierarchyName);
        if (pmAlreadyExist) {
            final String message =
                    String.format("The platform matcher with the name: %s and hierarchy: %s, already exists.",
                            matcherName, hierarchyName);
            log.warn(message);
            throw new HttpConflictException(message);
        }
        final DbMatcher dbMatcher = matcherService.getDbMatcher(matcherName);
        final DbHierarchy dbHierarchy = hierarchyService.getDbHierarchy(hierarchyName);
        final DbPlatformMatcher dbPlatformMatcher = matcherToDbPlatformMatcherMapper.map(matcher);
        dbPlatformMatcher.setMatcher(dbMatcher);
        dbPlatformMatcher.setHierarchy(dbHierarchy);
        platformMatcherRepository.save(dbPlatformMatcher);
        return matcher;
    }

    public Matcher updatePlatformMatcher(final Matcher matcher) {
        final String matcherName = matcher.getName();
        final String hierarchyName = matcher.getHierarchy();
        final DbPlatformMatcher dbPlatformMatcher =
                platformMatcherRepository.findDbPlatformMatcherByMatcher_NameAndHierarchy_Name(matcherName, hierarchyName)
                .orElseThrow(() -> new HttpNotFoundException(
                        String.format("The platform matcher with the name: %s and hierarchy: %s, does not exist",
                                matcherName, hierarchyName))
                );
        dbPlatformMatcher.setEnabled(matcher.isEnabled());
        dbPlatformMatcher.setIndex(matcher.getIndex());
        dbPlatformMatcher.setMatchLevel(matcher.getMatchLevel());
        platformMatcherRepository.save(dbPlatformMatcher);
        return matcher;
    }

    public List<PlatformMatcher> getPlatformMatchers() {
        List<DbPlatformMatcher> dbPlatformMatchers = platformMatcherRepository.findAll();
        return dbPlatformMatcherToPlatformMatcher.map(dbPlatformMatchers);
    }


    public List<PlatformMatcher> deletePlatformMatchers(final String matcherName, final String hierarchyName) {
        final DbPlatformMatcher dbPlatformMatcher = platformMatcherRepository.findByNameAndHierarchy(matcherName, hierarchyName);
        if (Objects.isNull(dbPlatformMatcher)) {
            throw new HttpNotFoundException(
                    String.format("The matcher %s with the hierarchy %s, does not exist", matcherName, hierarchyName));
        }
        platformMatcherRepository.delete(dbPlatformMatcher);
        return getPlatformMatchers();
    }
}
