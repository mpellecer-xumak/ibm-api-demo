package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import com.wdm.configuration.api.persistence.entity.DbClientAppend;
import com.wdm.configuration.api.persistence.repository.AppendRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppendServiceTest {
    private static final String APPEND_NAME = "myAppend";
    private static final Boolean ENABLED = Boolean.TRUE;
    private static final Boolean ENABLED_MODIFIED = Boolean.FALSE;
    private static final Boolean CHECK_DO_NOT_CALL = Boolean.FALSE;
    private static final Boolean CHECK_DO_NOT_CALL_MODIFIED = Boolean.TRUE;
    private static final Integer HOUSEHOLD_THRESHOLD = 95;
    private static final Integer HOUSEHOLD_THRESHOLD_MODIFIED = 90;


    @Mock
    private AppendRepository appendRepository;

    @Mock
    private Mapper<Append, DbAppend> toDbAppendMapper;

    @Mock
    private Mapper<DbAppend, Append> toAppendMapper;

    @InjectMocks
    private AppendService appendService;

    @Captor
    private ArgumentCaptor<DbAppend> dbAppendCaptor;

    @Test
    void testCreateAppend() {
        final Append append = getAppend();
        when(appendRepository.existsByName(APPEND_NAME)).thenReturn(false);

        final Append result = appendService.createAppend(append);
        assertEquals(append, result);
    }

    @Test
    void testCreateAppend_AlreadyExists() {
        final Append append = getAppend();
        when(appendRepository.existsByName(APPEND_NAME)).thenReturn(true);

        assertThrows(HttpConflictException.class, () -> appendService.createAppend(append));
    }

    @Test
    void testGetAppends() {
        final List<DbAppend> dbAppends = List.of(getDbAppend());
        when(appendRepository.findAll()).thenReturn(dbAppends);

        List<Append> result = appendService.getAppends();
        assertNotNull(result);
    }

    @Test
    void testGetAppends_WhenNoAppendsFound() {
        when(appendRepository.findAll()).thenReturn(List.of());
        assertThrows(HttpNotFoundException.class, () -> appendService.getAppends());
    }

    @Test
    void testDeleteAppend() {
        final var append = new DbAppend();
        when(appendRepository.findByName(APPEND_NAME)).thenReturn(append);
        appendService.deleteAppend(APPEND_NAME);
        verify(appendRepository).delete(append);
    }

    @Test
    void testDeleteAppendNotFound() {
        when(appendRepository.findByName(APPEND_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class,
                () -> appendService.deleteAppend(APPEND_NAME));
        verify(appendRepository, never()).delete(any());
    }

    @Test
    void testDeleteAppendWithClient() {
        final DbAppend append = new DbAppend();
        append.setClientAppends(List.of(new DbClientAppend()));
        when(appendRepository.findByName(APPEND_NAME)).thenReturn(append);
        assertThrows(HttpConflictException.class,
                () -> appendService.deleteAppend(APPEND_NAME));
        verify(appendRepository, never()).delete(any());
    }

    @Test
    void testUpdateAppend() {
        when(appendRepository.findByName(APPEND_NAME)).thenReturn(getDbAppend());
        when(appendRepository.save(any())).then(a -> a.getArguments()[0]);
        final Append append = getAppend();
        appendService.updateAppend(append);
        verify(appendRepository).save(dbAppendCaptor.capture());
        final DbAppend resultingAppend = dbAppendCaptor.getValue();

        assertEquals(ENABLED_MODIFIED, resultingAppend.getEnabled());
        assertEquals(HOUSEHOLD_THRESHOLD_MODIFIED, resultingAppend.getHouseholdThreshold());
        assertEquals(CHECK_DO_NOT_CALL_MODIFIED, resultingAppend.getCheckDoNotCall());

        assertNull(resultingAppend.getAddressThreshold());
        assertNull(resultingAppend.getIndividualThreshold());
        assertNull(resultingAppend.getDateOfBirthRangeYears());
        assertNull(resultingAppend.getEnableFuzzyGenderFilter());
        assertNull(resultingAppend.getEnableHouseholdLandline());
        assertNull(resultingAppend.getEnableHouseholdWireless());
        assertNull(resultingAppend.getEnableIndividualLandline());
        assertNull(resultingAppend.getEnableIndividualWireless());
        assertNull(resultingAppend.getEnableContactPointLandline());
        assertNull(resultingAppend.getEnableContactPointWireless());
        assertNull(resultingAppend.getResidentThreshold());
        assertNull(resultingAppend.getLastNameThreshold());
        assertNull(resultingAppend.getSuppressStateDoNotCall());
        assertNull(resultingAppend.getSuppressFederalDoNotCall());
        assertNull(resultingAppend.getReturnDoNotCallPhone());
    }

    @Test
    void testUpdateAppend_NotFound() {
        when(appendRepository.findByName(APPEND_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> appendService.updateAppend(getAppend()));
    }

    private Append getAppend() {
        final Append append = new Append();
        append.setName(APPEND_NAME);
        append.setEnabled(ENABLED_MODIFIED);
        append.setHouseholdThreshold(HOUSEHOLD_THRESHOLD_MODIFIED);
        append.setCheckDoNotCall(CHECK_DO_NOT_CALL_MODIFIED);
        return append;
    }

    private DbAppend getDbAppend() {
        final DbAppend dbAppend = new DbAppend();
        dbAppend.setEnabled(ENABLED);
        dbAppend.setCheckDoNotCall(CHECK_DO_NOT_CALL);
        dbAppend.setAddressThreshold(90);
        dbAppend.setHouseholdThreshold(HOUSEHOLD_THRESHOLD);
        dbAppend.setIndividualThreshold(91);
        dbAppend.setResidentThreshold(92);
        dbAppend.setLastNameThreshold(93);
        dbAppend.setDateOfBirthRangeYears(900);
        dbAppend.setSuppressStateDoNotCall(Boolean.TRUE);
        dbAppend.setSuppressFederalDoNotCall(Boolean.FALSE);
        dbAppend.setReturnDoNotCallPhone(Boolean.TRUE);
        dbAppend.setEnableFuzzyGenderFilter(Boolean.TRUE);
        dbAppend.setEnableHouseholdLandline(Boolean.FALSE);
        dbAppend.setEnableHouseholdWireless(Boolean.FALSE);
        dbAppend.setEnableIndividualLandline(Boolean.TRUE);
        dbAppend.setEnableIndividualWireless(Boolean.FALSE);
        dbAppend.setEnableContactPointLandline(Boolean.TRUE);
        dbAppend.setEnableContactPointWireless(Boolean.FALSE);
        return dbAppend;
    }
}
