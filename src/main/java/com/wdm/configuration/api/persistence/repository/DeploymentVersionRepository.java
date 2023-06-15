package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbDeploymentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentVersionRepository extends JpaRepository<DbDeploymentVersion, String> {
}
