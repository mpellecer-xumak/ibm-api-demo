package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbIdentityResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityResolutionRepository extends JpaRepository<DbIdentityResolution, Integer> {
}
