package com.wdm.configuration.api.service;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Deployment;
import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.persistence.entity.DbDeploymentVersion;
import com.wdm.configuration.api.persistence.repository.DeploymentVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VersionManagementService {
    private final DeploymentVersionRepository versionRepository;
    private final Mapper<DbDeploymentVersion, VersionManagement> mapper;


    public List<VersionManagement> getDeploymentVersions() {
        return mapper.map(versionRepository.findAll());
    }

    public VersionManagement getDeploymentVersion(final String deploymentName) {
        final Optional<DbDeploymentVersion> deploymentVersion = versionRepository.findById(deploymentName);
        if (deploymentVersion.isEmpty()) {
            return null;
        }
        return mapper.map(deploymentVersion.get());
    }

    public VersionManagement changeDeploymentVersion(final String entity, final String build){
        final Optional<DbDeploymentVersion> deploymentVersion = versionRepository.findById(entity);

        if (deploymentVersion.isEmpty()) {
            return null;
        }
        final DbDeploymentVersion dbDeploymentVersion = deploymentVersion.get();
        final String newState = dbDeploymentVersion.getColor().equals(Deployment.BLUE.name()) ? Deployment.GREEN.name() : Deployment.BLUE.name();
        dbDeploymentVersion.setColor(newState);
        dbDeploymentVersion.setBuild(build);
        versionRepository.save(dbDeploymentVersion);
        return getDeploymentVersion(entity);
    }
}
