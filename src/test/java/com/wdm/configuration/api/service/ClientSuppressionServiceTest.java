package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbSuppressionViewToSuppressionMapper;
import com.wdm.configuration.api.mapper.todbentity.SuppressionToDbClientSuppression;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientSuppression;
import com.wdm.configuration.api.persistence.entity.DbSuppression;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.repository.ClientSuppressionRepository;
import com.wdm.configuration.api.persistence.view.ClientSuppressionView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientSuppressionServiceTest {
    private static final String SUPP_NAME = "my-suppression";
    private static final String CLIENT_ID = "client-id";
    private static final String SUB_CLIENT_ID = "sub-client-id";

    @Mock
    private ClientService clientService;
    @Mock
    private SuppressionsService suppressionsService;
    @Mock
    private ClientSuppressionRepository clientSuppressionRepository;
    @Mock
    private SuppressionToDbClientSuppression toDbClientSuppression;
    @Mock
    private BaseViewRepository<ClientSuppressionView, String> suppressionViewRepository;
    @Mock
    private DbSuppressionViewToSuppressionMapper suppressionMapper;

    @InjectMocks
    private ClientSuppressionService clientSuppressionService;

    @Test
    void associateSuppression() {
        final Suppression suppression = getSuppression();
        when(clientSuppressionRepository.existsClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(false);
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(new DbClient());
        when(suppressionsService.getDbSuppression(SUPP_NAME)).thenReturn(new DbSuppression());
        when(toDbClientSuppression.map(suppression)).thenReturn(new DbClientSuppression());

        final Suppression result = clientSuppressionService.associateSuppression(CLIENT_ID, SUB_CLIENT_ID, suppression);

        assertEquals(suppression, result);
    }

    @Test
    void associateSuppression_AlreadyAssociated() {
        final Suppression suppression = getSuppression();
        when(clientSuppressionRepository.existsClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(true);

        assertThrows(HttpConflictException.class, () -> clientSuppressionService.associateSuppression(CLIENT_ID, SUB_CLIENT_ID, suppression));

        verifyNoInteractions(clientService, suppressionsService, toDbClientSuppression);
    }

    @Test
    void updateSuppresion(){
        Suppression suppression = getSuppression();
        DbClientSuppression dbSuppression = getDbSuppression();
        when(clientSuppressionRepository.getClientSuppression(suppression.getName(), CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbSuppression);
        when(toDbClientSuppression.map(suppression)).thenReturn(dbSuppression);
        when(suppressionViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(getSetSuppresion());
        Mockito.lenient().when(suppressionMapper.map(getSetSuppresion())).thenReturn(getSuppressions());
        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(getSuppressions());

        List<Suppression> result = clientSuppressionService.updateClientSuppresion(suppression, CLIENT_ID, SUB_CLIENT_ID);
        assertEquals(getSuppressions(), result);
    }

    @Test
    void deleteSuppression(){
        final DbClientSuppression dbSuppression = getDbSuppression();
        when(clientSuppressionRepository.getClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbSuppression);
        when(suppressionViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(getSetSuppresion());
        Mockito.lenient().when(suppressionMapper.map(getSetSuppresion())).thenReturn(getSuppressions());
        when(clientSuppressionService.getSuppressionClientConfig(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(getSuppressions());

        List<Suppression> result = clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID);
        assertEquals(getSuppressions(), result);
        verify(clientSuppressionRepository).delete(dbSuppression);
    }

    @Test
    void deleteNotFoundSuppression(){
        final DbClientSuppression dbSuppression = getDbSuppression();
        when(clientSuppressionRepository.getClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);

        assertThrows(HttpNotFoundException.class, () -> {
            clientSuppressionService.deleteClientSuppression(SUPP_NAME, CLIENT_ID, SUB_CLIENT_ID);
        });
    }


    private Suppression getSuppression() {
        return Suppression.builder()
                .name(SUPP_NAME)
                .build();
    }

    private Set<ClientSuppressionView> getSetSuppresion() {
        Set<ClientSuppressionView> suppressionViews = new HashSet<>();
        suppressionViews.add(new ClientSuppressionView());
        return suppressionViews;
    }

    private DbClientSuppression getDbSuppression() {
        DbClientSuppression dbClientSuppression = new DbClientSuppression();
        dbClientSuppression.setSuppression(new DbSuppression());
        dbClientSuppression.setClient(new DbClient());
        dbClientSuppression.setId(1);
        return dbClientSuppression;
    }

    private List<Suppression> getSuppressions() {
        final List<Suppression> suppressions = new ArrayList<>();
        suppressions.add(getSuppression());
        return suppressions;
    }

}