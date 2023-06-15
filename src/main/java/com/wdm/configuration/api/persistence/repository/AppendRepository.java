package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppendRepository extends JpaRepository<DbAppend, Integer> {
    DbAppend findByName(final String name);

    boolean existsByName(final String name);
}
