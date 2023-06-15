package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BundleRepository extends JpaRepository<DbBundle, Integer> {
    boolean existsByName(final String name);
    List<DbBundle> findByNameIn(List<String> names);

    DbBundle findByName(String name);

    DbBundle findFirstByName(String name);
}
