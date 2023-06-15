package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatcherRepository extends JpaRepository<DbMatcher, Integer> {
    List<DbMatcher> findAllByNameIn(final List<String> names);

    DbMatcher findByName(final String name);
}
