package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HierarchyRespository extends JpaRepository<DbHierarchy, Integer> {
    DbHierarchy findByName(final String name);
    List<DbHierarchy> findAllByNameIn(final List<String> names);
}
