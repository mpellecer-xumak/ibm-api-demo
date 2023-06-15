package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Deployment;
import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.persistence.entity.DbDeploymentVersion;

class DbDeploymentToVersionManagementMapperTest extends BaseMapperTest<DbDeploymentVersion, VersionManagement> {
    private static final String VERSION_NAME = "Email";
    private static final String COLOR = "BLUE";

    @Override
    protected Mapper<DbDeploymentVersion, VersionManagement> getMapper(ModelMapper modelMapper) {
        return new DbDeploymentToVersionManagementMapper(modelMapper);
    }

    @Test
    void testMap() {
        final var deploymentVersion = getDeploymentVersion();
        final var result = mapper.map(deploymentVersion);
        assertEquals(VERSION_NAME, result.getEntity());
        assertEquals(Deployment.BLUE, result.getState());
    }

    private DbDeploymentVersion getDeploymentVersion() {
        final var deployment = new DbDeploymentVersion();
        deployment.setName(VERSION_NAME);
        deployment.setColor(COLOR);
        return deployment;
    }


}