package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.mapper.fromdbentity.DbAggregateBundleToBundleMapper;
import com.wdm.configuration.api.mapper.fromdbentity.DbBundleToBundleMapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.BundleType;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.BundleRepository;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class BundlesService {

    private final DbBundleToBundleMapper bundlesMapper;
    private final DbAggregateBundleToBundleMapper aggregateMapper;
    
    private final BaseViewRepository<ClientAggregateView, String> aggregateRepository;
    private final BundleRepository bundleRepository;

    private final AttributeService attributeService;
    private final ClientService clientService;

    @Transactional
    public Bundle getBundle(String bundleName) {
        final DbBundle bundle = bundleRepository.findByName(bundleName);

        if (Objects.isNull(bundle)) {
            final String message = "No bundle was found";
            log.warn(message);

            throw new HttpNotFoundException(message);
        }

        return bundlesMapper.map(bundle);
    }

    @Transactional
    public List<Bundle> getBundleNames() {
        List<DbBundle> dbBundleList = bundleRepository.findAll();

        if (CollectionUtils.isEmpty(dbBundleList)) {
            final String message = "No bundles were found";
            log.warn(message);

            throw new HttpNotFoundException(message);
        }

        return bundlesMapper.map(dbBundleList);
    }

    @Transactional
    public Set<Bundle> getBundles(final String clientId) {
        return getBundles(clientId, null);
    }

    @Transactional
    public Set<Bundle> getBundles(final String clientId, final String subClientId) {
        final DbClient client = clientService.getClient(clientId, subClientId);
        return getBundles(client).stream().peek(bundle -> bundle.setAttributes(null)).collect(Collectors.toSet());
    }

    @Transactional
    public Bundle getBundle(final String clientId, final String bundleName) {
        return getBundle(clientId, null, bundleName);
    }

    @Transactional
    public Bundle getBundle(final String clientId, final String subClientId, final String bundleName) {
        final DbClient client = clientService.getClient(clientId, subClientId);
        return getBundle(bundleName, client);
    }

    public Bundle createBundle(final Bundle bundle) {
        final boolean alreadyExists = bundleRepository.existsByName(bundle.getName());
        if (alreadyExists) {
            return null;
        }
        final List<DbAttribute> bundleAttributes = attributeService.createAttributes(bundle.getAttributes());
        final DbBundle dbBundle = new DbBundle();
        dbBundle.setName(bundle.getName());
        dbBundle.setAttributes(bundleAttributes);
        final DbBundle savedBundle = bundleRepository.save(dbBundle);
        return bundlesMapper.map(savedBundle);
    }

    public Bundle removeAttributeBundle(final String bundleName, final String attributeName) {
        final DbBundle dbBundle = bundleRepository.findByName(bundleName);

        if (Objects.isNull(dbBundle)) {
            final String message = "No bundles were found";
            log.warn(message);

            throw new HttpNotFoundException(message);
        }

        final DbAttribute attribute = dbBundle.getAttributes().stream()
            .filter(dbAttribute -> dbAttribute.getName().equals(attributeName))
            .findFirst()
            .orElse(null);

        if (Objects.isNull(attribute)) {
            final String message = String.format("The Attribute '%s' does not exist in the Bundle", attributeName);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }

        dbBundle.getAttributes().remove(attribute);
        final DbBundle savedBundle = bundleRepository.save(dbBundle);
        return bundlesMapper.map(savedBundle);
    }

    private void throwIfNoBundleWasProvided(List<SimpleName> bundleNames) {
        if (CollectionUtils.isEmpty(bundleNames)) {
            final String message = "Providing empty bundle list to be associated";
            log.debug(message);
            throw new HttpConflictException(message);
        }

    }

    public List<Bundle> assignHumanInsightBundlesToClient(
            final String clientId,
            final String subClientId,
            final List<SimpleName> bundleNames) {

        throwIfNoBundleWasProvided(bundleNames);

        final DbClient client = clientService.getClient(clientId, subClientId);
        final Set<String> clientBundlesNames = client.getHumanInsightBundles()
                .stream()
                .map(DbBundle::getName)
                .collect(Collectors.toSet());
        final List<String> bundlesToAssociate = bundleNames.stream()
                .map(SimpleName::getName)
                .filter(Predicate.not(clientBundlesNames::contains))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bundlesToAssociate)) {
            final String message = "All bundles are already associated";
            log.info(message);
            throw new HttpConflictException(message);
        }
        final List<DbBundle> dbBundles = matchBundlesByNames(bundlesToAssociate);
        client.getHumanInsightBundles().addAll(dbBundles);
        clientService.save(client);
        return bundlesMapper.map(client.getHumanInsightBundles());
    }

    public List<Bundle> getHumanInsightsBundles(final String clientId, final String subClientId) {
        final DbClient client = clientService.getClient(clientId, subClientId);
        return bundlesMapper.map(client.getHumanInsightBundles());
    }

    public List<DbBundle> matchBundlesByNames(final List<String> bundleNames) {
        final List<DbBundle> dbBundles = bundleRepository.findByNameIn(bundleNames);
        final boolean requestedBundlesExist = bundleNames.size() == dbBundles.size();
        if (CollectionUtils.isEmpty(dbBundles) || !requestedBundlesExist) {
            final String message = "There are non-existent bundles in the request";
            log.info(message, bundleNames);
            throw new HttpConflictException(message);
        }
        return dbBundles;
    }

    public boolean isBundleAssociated(final DbBundle dbBundle) {
        return !CollectionUtils.isEmpty(dbBundle.getHumanInsightClients())
                || !CollectionUtils.isEmpty(dbBundle.getAggregateBundles());
    }

    public void deleteBundle(final String bundleName) {
        final DbBundle dbBundle = bundleRepository.findFirstByName(bundleName);
        if ( dbBundle == null) {
            String message = String.format("The bundle '%s' does not exist", bundleName);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        if (isBundleAssociated(dbBundle)) {
            String message = String.format("Bundle '%s' cannot be deleted due to a conflict with the data", bundleName);
            log.warn(message);
            throw new HttpConflictException(message);
        }

        bundleRepository.delete(dbBundle);
    }

    private Set<Bundle> getBundles(final DbClient client) {
        if (client == null) {
            return Collections.emptySet();
        }
        final Set<ClientAggregateView> aggregateViews = aggregateRepository.findAll(client.getClientId(),
                client.getSubClientId());
        final List<DbBundle> dbInsightBundles = client.getHumanInsightBundles();
        final List<Bundle> insightBundles = getBundles(bundlesMapper, dbInsightBundles, BundleType.HUMAN_INSIGHT);
        final List<Bundle> aggregateBundles = getBundles(aggregateMapper, aggregateViews, BundleType.AGGREGATE);
        return Stream.of(insightBundles, aggregateBundles)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    private <T> List<Bundle> getBundles(final Mapper<T, Bundle> mapper,
            final Collection<T> sourceList,
            final BundleType bundleType) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        return mapper.map(sourceList).stream()
                .peek(bundle -> bundle.setBundleType(bundleType))
                .collect(Collectors.toList());
    }

    private Bundle getBundle(final String bundleName, final DbClient client) {
        final Set<Bundle> bundles = getBundles(client);
        final Optional<Bundle> optionalBundle = bundles.stream()
                .filter(bundle -> bundleName.equalsIgnoreCase(bundle.getName()))
                .findFirst();
        if (optionalBundle.isEmpty()) {
            return null;
        }
        return optionalBundle.get();
    }

    public List<Bundle> removeClientBundles(
            final String clientId,
            final String subClientId,
            final List<SimpleName> bundleNames) {

        throwIfNoBundleWasProvided(bundleNames);

        final DbClient client = clientService.getClient(clientId, subClientId);
        final Set<String> bundleNamesSet = bundleNames.stream()
                .map(SimpleName::getName)
                .collect(Collectors.toSet());
        final List<DbBundle> allBundles = client.getHumanInsightBundles();
        final List<DbBundle> bundlesToDelete = allBundles.stream()
                .filter(b -> bundleNamesSet.contains(b.getName()))
                .collect(Collectors.toList());

        if(bundleNamesSet.size() != bundlesToDelete.size()) {
            final Set<String> dbBundlesName = bundlesToDelete.stream()
                    .map(DbBundle::getName)
                    .collect(Collectors.toSet());
            final List<String> notFoundBundles = bundleNamesSet.stream()
                    .filter(Predicate.not(dbBundlesName::contains))
                    .collect(Collectors.toList());
            throw new HttpNotFoundException("At least one given bundle name wasn't found. "+notFoundBundles);
        }

        allBundles.removeAll(bundlesToDelete);
        client.setHumanInsightBundles(allBundles);
        clientService.save(client);
        return bundlesMapper.map(bundlesToDelete);
    }

    public Bundle updateBundle(final Bundle bundle) {
        final DbBundle dbBundle = getDbBundle(bundle.getName());
        final List<Attribute> attributes = bundle.getAttributes();
        final List<DbAttribute> dbAttributes = attributeService.createAttributes(attributes);
        dbBundle.setAttributes(dbAttributes);
        bundleRepository.save(dbBundle);
        return bundle;
    }

    private DbBundle getDbBundle(final String name) {
        final DbBundle dbBundle = bundleRepository.findByName(name);
        if (Objects.isNull(dbBundle)) {
            throw new HttpNotFoundException("The bundle doesn't exist");
        }
        return dbBundle;
    }
}
