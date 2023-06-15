package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbSuppression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SuppressionRepository extends JpaRepository<DbSuppression, Integer> {
    Set<DbSuppression> findAllByEnabledIsTrue();

    DbSuppression findByName(final String name);
}
