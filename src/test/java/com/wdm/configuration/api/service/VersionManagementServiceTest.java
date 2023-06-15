package com.wdm.configuration.api.service;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.persistence.entity.DbDeploymentVersion;
import com.wdm.configuration.api.persistence.repository.DeploymentVersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VersionManagementServiceTest {
    private static final String ENTITY_NAME = "Email";

    @Mock
    private DeploymentVersionRepository versionRepository;
    @Mock
    private Mapper<DbDeploymentVersion, VersionManagement> mapper;

    @InjectMocks
    private VersionManagementService service;

    @Test
    void testGetDeploymentVersion() {
        final var deploymentVersion = new DbDeploymentVersion();
        final var vm = new VersionManagement();
        vm.setEntity(ENTITY_NAME);
        when(versionRepository.findById(ENTITY_NAME)).thenReturn(Optional.of(deploymentVersion));
        when(mapper.map(deploymentVersion)).thenReturn(vm);

        final var result = service.getDeploymentVersion(ENTITY_NAME);
        assertEquals(ENTITY_NAME, result.getEntity());
    }

    @Test
    void testGetDeploymentVersionNonExistent() {
        when(versionRepository.findById(ENTITY_NAME)).thenReturn(Optional.empty());

        final var result = service.getDeploymentVersion(ENTITY_NAME);
        assertNull(result);
    }
}