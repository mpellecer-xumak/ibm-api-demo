package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbMatcherViewToMatcherMapper;
import com.wdm.configuration.api.mapper.todbentity.MatcherToDbClientMatcherMapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientMatcher;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.ClientMatcherRepository;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientMatcherService {
    private final ClientService clientService;
    private final MatcherService matcherService;
    private final HierarchyService hierarchyService;
    private final ClientMatcherRepository clientMatcherRepository;
    private final MatcherToDbClientMatcherMapper toDbClientMatcherMapper;
    private final DbMatcherViewToMatcherMapper matcherMapper;
    private final BaseViewRepository<ClientMatcherView, String> matcherViewRepository;

    public Matcher associateMatcher(final String clientId, final String subClientId, final Matcher matcher) {
        final boolean clientMatcherExists = clientMatcherRepository.doesClientMatcherExists(
                clientId, subClientId, matcher.getName(), matcher.getHierarchy());
        if (clientMatcherExists) {
            final String message = "The client matcher already exists";
            log.warn(message, matcher.getName(), matcher.getHierarchy());
            throw new HttpConflictException(message);
        }
        final DbClientMatcher dbClientMatcher = toDbClientMatcherMapper.map(matcher);
        final DbClient dbClient = clientService.getClient(clientId, subClientId);
        final DbMatcher dbMatcher = matcherService.getDbMatcher(matcher.getName());
        final DbHierarchy dbHierarchy = hierarchyService.getDbHierarchy(matcher.getHierarchy());
        dbClientMatcher.setClient(dbClient);
        dbClientMatcher.setMatcher(dbMatcher);
        dbClientMatcher.setHierarchy(dbHierarchy);
        clientMatcherRepository.save(dbClientMatcher);
        return matcher;
    }

    public List<MatcherHierarchyGroup> getMachersClientConfig(final String clientId, final String subClientId) {
        final Set<ClientMatcherView> matcherViews = matcherViewRepository.findAll(clientId, subClientId);
        if (CollectionUtils.isEmpty(matcherViews)) {
            throw new HttpNotFoundException("Client does not have matchers associated");
        }
        return matcherMapper.map(matcherViews);
    }

    public Matcher updateMatcher(final String clientId, final String subClientId, final Matcher matcher) {
        final DbClientMatcher dbClientMatcher = clientMatcherRepository.findClientMatcher(
                clientId, subClientId, matcher.getName(), matcher.getHierarchy());
        if (Objects.isNull(dbClientMatcher)) {
            throw new HttpNotFoundException(
                    String.format("The client doesn't have the matcher %s with the hierarchy %s, associated",
                        matcher.getName(), matcher.getHierarchy()));
        }
        dbClientMatcher.setIndex(matcher.getIndex());
        dbClientMatcher.setMatchLevel(matcher.getMatchLevel());
        dbClientMatcher.setEnabled(matcher.isEnabled());
        clientMatcherRepository.save(dbClientMatcher);
        return matcher;
    }

    public List<MatcherHierarchyGroup> deleteClientMatcher(final String clientId, final String subClientId, final ClientMachersDeleteRequest deleteRequest) {
        final DbClientMatcher dbClientMatcher = clientMatcherRepository.findClientMatcher(
                clientId, subClientId, deleteRequest.getMatcherName(), deleteRequest.getHierarchyName());
        if (Objects.isNull(dbClientMatcher)) {
            throw new HttpNotFoundException(
                    String.format("The client doesn't have the matcher %s with the hierarchy %s, associated",
                            deleteRequest.getMatcherName(), deleteRequest.getHierarchyName()));
        }

        clientMatcherRepository.delete(dbClientMatcher);
        return getMachersClientConfig(clientId, subClientId);
    }
}
