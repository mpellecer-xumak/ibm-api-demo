package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AggregateRepository extends JpaRepository<DbAggregate, Integer> {
    List<DbAggregate> findAllByNameIn(final List<String> names);

    DbAggregate findByName(final String name);
}
