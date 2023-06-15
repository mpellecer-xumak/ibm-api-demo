package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<DbAttribute, Integer> {
    List<DbAttribute> findByNameIn(List<String> names);
    Optional<DbAttribute> findByName(String name);
}
