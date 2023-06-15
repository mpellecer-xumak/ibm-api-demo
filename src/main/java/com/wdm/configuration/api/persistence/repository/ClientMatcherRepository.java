package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbClientMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("PMD.UseObjectForClearerAPI")
public interface ClientMatcherRepository extends JpaRepository<DbClientMatcher, Integer> {
    boolean existsByClient_ClientIdAndClient_SubClientIdAndMatcher_NameAndHierarchy_Name(
            final String clientId, final String subClientId, final String matcherName, final String hierarchyName);

    DbClientMatcher findByClient_ClientIdAndClient_SubClientIdAndMatcher_NameAndHierarchy_Name(
            final String clientId, final String subClientId, final String matcherName, final String hierarchyName);

    default boolean doesClientMatcherExists(
            final String clientId, final String subClientId, final String matcherName, final String hierarchyName) {
        return existsByClient_ClientIdAndClient_SubClientIdAndMatcher_NameAndHierarchy_Name(
                clientId, subClientId, matcherName, hierarchyName);
    }

    default DbClientMatcher findClientMatcher(
            final String clientId, final String subClientId, final String matcherName, final String hierarchyName) {
        return findByClient_ClientIdAndClient_SubClientIdAndMatcher_NameAndHierarchy_Name(
                clientId, subClientId, matcherName, hierarchyName);
    }

}
