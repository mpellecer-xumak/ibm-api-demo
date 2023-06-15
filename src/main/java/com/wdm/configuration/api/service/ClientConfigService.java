package com.wdm.configuration.api.service;


import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.*;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.view.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@SuppressWarnings("PMD")
public class ClientConfigService {
    private final BaseViewRepository<IdentityResolutionClientConfigView, String> clientConfigViewRepository;
    private final BaseViewRepository<ClientMatcherView, String> matcherViewRepository;
    private final BaseViewRepository<ClientSuppressionView, String> suppressionViewRepository;
    private final BaseViewRepository<ClientAppendView, String> appendViewRepository;
    private final BaseViewRepository<ClientAggregateView, String> aggregateViewRepository;

    private final BundlesService bundlesService;

    private final Mapper<IdentityResolutionClientConfigView, ClientConfig> clientConfigMapper;
    private final Mapper<ClientMatcherView, MatcherHierarchyGroup> matcherMapper;
    private final Mapper<ClientSuppressionView, Suppression> suppressionMapper;
    private final Mapper<ClientAppendView, Append> appendMapper;
    private final Mapper<ClientAggregateView, Aggregate<Bundle>> aggregateBundleMapper;

    @Transactional
    public ClientConfig getClientConfig(final String clientId, final String subClientId) {
        final IdentityResolutionClientConfigView result = clientConfigViewRepository.find(clientId, subClientId);
        if (result == null) {
            return null;
        }
        final ClientConfig clientConfig = clientConfigMapper.map(result);
        clientConfig.setBundles(bundlesService.getHumanInsightsBundles(clientId, subClientId));
        clientConfig.setMatchers(getMatchers(clientId, subClientId));
        clientConfig.setAggregates(getAggregateBundleMap(clientId, subClientId));
        clientConfig.setSuppressions(getSuppressionMap(clientId, subClientId));
        clientConfig.setAppends(getAppendMap(clientId, subClientId));
        return clientConfig;
    }

    @Transactional
    public ClientConfig getClientConfig(final String clientId) {
        return getClientConfig(clientId, null);
    }

    private List<MatcherHierarchyGroup> getMatchers(final String clientId, final String subClientId) {
        final Set<ClientMatcherView> matcherViews = matcherViewRepository.findAll(clientId, subClientId);
        return matcherMapper.map(matcherViews);
    }

    private Map<String, Aggregate<Bundle>> getAggregateBundleMap(final String clientId, final String subClientId) {
        final Set<ClientAggregateView> aggregateViews = aggregateViewRepository.findAll(clientId, subClientId);
        final List<Aggregate<Bundle>> mappedAggregates = aggregateBundleMapper.map(aggregateViews);
        return mappedAggregates.stream().collect(Collectors.toMap(Aggregate::getName, Function.identity()));
    }

    private Map<String, Suppression> getSuppressionMap(final String clientId, final String subClientId) {
        final Set<ClientSuppressionView> suppressionViews = suppressionViewRepository.findAll(clientId, subClientId);
        final List<Suppression> mappedSuppressions = suppressionMapper.map(suppressionViews);
        return mappedSuppressions.stream().collect(Collectors.toMap(Suppression::getName, Function.identity()));
    }

    private Map<String, Append> getAppendMap(final String clientId, final String subClientId) {
        final Set<ClientAppendView> suppressionViews = appendViewRepository.findAll(clientId, subClientId);
        final List<Append> mappedAppends = appendMapper.map(suppressionViews);
        return mappedAppends.stream().collect(Collectors.toMap(Append::getName, Function.identity()));
    }

}
