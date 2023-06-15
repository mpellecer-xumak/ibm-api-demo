package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.entity.DbClientSuppression;
import com.wdm.configuration.api.persistence.entity.DbSuppression;
import com.wdm.configuration.api.persistence.repository.SuppressionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuppressionsServiceTest {
    private static final String SUPPRESSION_NAME = "my-suppression";
    private static final Integer RESIDENT_THRESHOLD = 61;
    private static final Integer RESIDENT_THRESHOLD_MODIFIED = 62;
    private static final Integer HOUSEHOLD_THRESHOLD = 71;
    private static final Integer HOUSEHOLD_THRESHOLD_MODIFIED = 72;
    private static final Integer DATE_OF_BIRTH_RANGE = 81;
    private static final Integer DATE_OF_BIRTH_RANGE_MODIFIED = 82;
    private static final Integer ADDRESS_THRESHOLD = 91;
    private static final Integer ADDRESS_THRESHOLD_MODIFIED = 92;

    @Mock
    private Mapper<Suppression, DbSuppression> suppressionMapper;
    @Mock
    private SuppressionRepository suppressionRepository;
    @InjectMocks
    private SuppressionsService service;
    @Captor
    private ArgumentCaptor<DbSuppression> dbSuppressionCaptor;

    @Test
    void testCreateSuppressions() {
        final var dbSuppressions = List.of(getDbSuppression());
        when(suppressionMapper.map(anyList())).thenReturn(dbSuppressions);
        when(suppressionRepository.saveAll(dbSuppressions)).thenReturn(dbSuppressions);

        final var result = service.createSuppressions(List.of(getSuppression()));

        assertEquals(1, result.size());
        assertEquals(SUPPRESSION_NAME, result.get(0).getName());
    }

    @Test
    void testCreateSuppressions_NullParam() {
        final var result = service.createSuppressions(null);

        verifyNoInteractions(suppressionMapper, suppressionRepository);
        assertEquals(0, result.size());
    }

    @Test
    void testCreateSuppressions_NoSavedInstances() {
        final var dbSuppressions = List.of(getDbSuppression());
        when(suppressionMapper.map(anyList())).thenReturn(dbSuppressions);
        when(suppressionRepository.saveAll(dbSuppressions)).thenReturn(null);

        final var result = service.createSuppressions(List.of(getSuppression()));

        assertEquals(0, result.size());
    }

    @Test
    void testGetDbSuppression() {
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(new DbSuppression());

        final DbSuppression result = service.getDbSuppression(SUPPRESSION_NAME);

        assertNotNull(result);
    }

    @Test
    void testGetDbSuppression_NotFound() {
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(null);

        assertThrows(HttpNotFoundException.class, () -> service.getDbSuppression(SUPPRESSION_NAME));
    }


    @Test
    void testDeleteSuppression() {
        final var suppression = new DbSuppression();
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(suppression);
        service.deleteSuppression(SUPPRESSION_NAME);
        verify(suppressionRepository).delete(suppression);
    }

    @Test
    void testDeleteSuppressionNotFound() {
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class,
                () -> service.deleteSuppression(SUPPRESSION_NAME));
        verify(suppressionRepository, never()).delete(any());
    }

    @Test
    void testDeleteSuppressionWithClient() {
        final DbSuppression suppression = new DbSuppression();
        suppression.setClientSuppressions(List.of(new DbClientSuppression()));
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(suppression);
        assertThrows(HttpConflictException.class,
                () -> service.deleteSuppression(SUPPRESSION_NAME));
        verify(suppressionRepository, never()).delete(any());
    }

    @Test
    void testUpdateSuppression() {
        final DbSuppression dbSuppression = getDbSuppression(); 
        final Suppression suppression = getSuppression();
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(dbSuppression);
        when(suppressionRepository.save(any())).thenAnswer(a -> a.getArguments()[0]);

        service.updateSuppression(suppression);
        verify(suppressionRepository).save(dbSuppressionCaptor.capture());

        final DbSuppression modifiedSuppression = dbSuppressionCaptor.getValue();

        assertEquals(RESIDENT_THRESHOLD_MODIFIED, modifiedSuppression.getResidentThreshold());
        assertEquals(HOUSEHOLD_THRESHOLD_MODIFIED, modifiedSuppression.getHouseholdThreshold());
        assertEquals(DATE_OF_BIRTH_RANGE_MODIFIED, modifiedSuppression.getDateOfBirthRangeYears());
        assertEquals(ADDRESS_THRESHOLD_MODIFIED, modifiedSuppression.getAddressThreshold());
        assertNull(modifiedSuppression.getEnableFuzzyGenderFilter());
    }

    @Test
    void testUpdateSuppressionNotFound() {
        when(suppressionRepository.findByName(SUPPRESSION_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> service.updateSuppression(getSuppression()));
    }

    private Suppression getSuppression() {
        return Suppression.builder()
                .name(SUPPRESSION_NAME)
                .residentThreshold(RESIDENT_THRESHOLD_MODIFIED)
                .householdThreshold(HOUSEHOLD_THRESHOLD_MODIFIED)
                .dateOfBirthRangeYears(DATE_OF_BIRTH_RANGE_MODIFIED)
                .addressThreshold(ADDRESS_THRESHOLD_MODIFIED)
                .build();
    }

    private DbSuppression getDbSuppression() {
        final var dbSuppression = new DbSuppression();
        dbSuppression.setName(SUPPRESSION_NAME);
        dbSuppression.setEnabled(Boolean.TRUE);
        dbSuppression.setEnableFuzzyGenderFilter(Boolean.FALSE);
        dbSuppression.setResidentThreshold(RESIDENT_THRESHOLD);
        dbSuppression.setHouseholdThreshold(HOUSEHOLD_THRESHOLD);
        dbSuppression.setDateOfBirthRangeYears(DATE_OF_BIRTH_RANGE);
        dbSuppression.setAddressThreshold(ADDRESS_THRESHOLD);
        return dbSuppression;
    }
}
