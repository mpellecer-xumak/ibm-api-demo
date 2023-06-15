package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbMatcherToMatcherMapper;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbMatcherMapper;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.repository.MatcherRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MatcherService {
    private final MatcherRepository matcherRepository;
    private final MatcherToDbMatcherMapper toDbMatcherMapper;
    private final DbMatcherToMatcherMapper toMatcherMapper;

    public List<SimpleName> createMatchers(final List<SimpleName> matchers) {
        final List<String> matcherNames = matchers.stream().map(SimpleName::getName).collect(Collectors.toList());
        final List<DbMatcher> existingMatcher = matcherRepository.findAllByNameIn(matcherNames);
        if (!CollectionUtils.isEmpty(existingMatcher)) {
            final String message = "There are existing matchers in the list";
            log.warn(message, matcherNames);
            throw new HttpConflictException(message);
        }
        final List<DbMatcher> matchersToInsert = toDbMatcherMapper.map(matchers);
        final List<DbMatcher> insertedMatchers = matcherRepository.saveAll(matchersToInsert);
        return toMatcherMapper.map(insertedMatchers);
    }

    public List<SimpleName> getExistingMatchers() {
        final List<DbMatcher> dbMatchers = matcherRepository.findAll();
        return toMatcherMapper.map(dbMatchers);
    }

    public DbMatcher getDbMatcher(final String name) {
        final DbMatcher dbMatcher = matcherRepository.findByName(name);
        if (Objects.isNull(dbMatcher)) {
            final String message = String.format("The matcher '%s' does not exists", name);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return dbMatcher;
    }

    public void deleteMatcher(String matcherName) {
        final DbMatcher dbMatcher = getDbMatcher(matcherName);

        if (!CollectionUtils.isEmpty(dbMatcher.getClientMatchers())) {
            throw new HttpConflictException(String.format("Cannot delete matcher '%s' while is in use by client.", matcherName));
        }

        if (!CollectionUtils.isEmpty(dbMatcher.getPlatformMatchers())) {
            throw new HttpConflictException(String.format("Cannot delete matcher '%s' while is in use by client.", matcherName));
        }
        matcherRepository.delete(dbMatcher);
    }
}
