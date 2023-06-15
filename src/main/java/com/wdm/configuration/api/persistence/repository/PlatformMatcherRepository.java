package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PlatformMatcherRepository extends JpaRepository<DbPlatformMatcher, Integer> {
    Set<DbPlatformMatcher> findAllByEnabledIsTrue();

    boolean existsByMatcher_NameAndHierarchy_Name(final String matcherName, final String hierarchyName);

    Optional<DbPlatformMatcher> findDbPlatformMatcherByMatcher_NameAndHierarchy_Name(final String matcherName, final String hierarchyName);

    default DbPlatformMatcher findByNameAndHierarchy(final String matcherName, final String hierarchyName){
        return findDbPlatformMatcherByMatcher_NameAndHierarchy_Name(matcherName, hierarchyName).orElse(null);
    }
}
