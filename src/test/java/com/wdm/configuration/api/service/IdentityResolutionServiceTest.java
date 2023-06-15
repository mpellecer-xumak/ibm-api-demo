package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolution;
import com.wdm.configuration.api.persistence.repository.IdentityResolutionRepository;
import com.wdm.configuration.api.request.IdentityRequest;

@ExtendWith(MockitoExtension.class)
public class IdentityResolutionServiceTest {
    @Mock
    private IdentityResolutionRepository identityResolutionRepository;
    @Mock
    private Mapper<DbIdentityResolution, IdentityRequest> mapper;
    @InjectMocks
    private IdentityResolutionService service;

    @Test
    void testGetIdentityResolutionConfiguration(){
        final var dbIdentityResolution = new DbIdentityResolution();
        final var IdentityRequest = new IdentityRequest();
        when(identityResolutionRepository.findAll()).thenReturn(List.of(dbIdentityResolution));
        when(mapper.map(List.of(dbIdentityResolution))).thenReturn(List.of(IdentityRequest));
        final var result = service.getIdentityResolutionConfiguration();
        assertNotNull(result);
    }
    @Test
    void testGetIdentityResolutionConfigurationNonExistent() {
        when(identityResolutionRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(HttpNotFoundException.class, ()->service.getIdentityResolutionConfiguration());
    }

    @Test
    void testPutIdentityResolutionConfiguration(){
        final var dbIdentityResolution = new DbIdentityResolution();
        final var dbIdentityResolutionList = List.of(dbIdentityResolution);
        final IdentityRequest config = new IdentityRequest(1,1,1,1,1,1,1,1,1,false,false,false,false,false,false);
        when(identityResolutionRepository.findAll()).thenReturn(dbIdentityResolutionList);
        when(identityResolutionRepository.save(dbIdentityResolution)).thenReturn(dbIdentityResolution);
        when(mapper.map(dbIdentityResolution)).thenReturn(config);
        final IdentityRequest result = service.putProvidedConfiguration(config);
        assertNotNull(result);

    }

    @Test
    void testPutIdentityResolutionConfigurationConflict(){
        assertThrows(HttpConflictException.class, ()->service.putProvidedConfiguration(null));
    }

}
