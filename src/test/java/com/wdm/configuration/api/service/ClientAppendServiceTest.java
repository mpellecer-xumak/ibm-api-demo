package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.persistence.repository.ClientAppendViewRepository;
import com.wdm.configuration.api.persistence.view.ClientAppendView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientAppend;
import com.wdm.configuration.api.persistence.repository.ClientAppendRespository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ClientAppendServiceTest {
    private static final String APPEND_NAME = "myAppend";
    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";

    @Mock
    private ClientService clientService;
    @Mock
    private AppendService appendService;
    @Mock
    private ClientAppendRespository clientAppendRespository;
    @Mock
    private ClientAppendViewRepository appendViewRepository;
    @Mock
    private Mapper<Append, DbClientAppend> toDbClientAppendMapper;
    @InjectMocks
    private ClientAppendService clientAppendService;

    @Test
    void testAssociateAppend() {
        final Append append = new Append();
        final DbClientAppend dbClientAppend = new DbClientAppend();
        append.setName(APPEND_NAME);
        when(clientAppendRespository.existsClientAppend(APPEND_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(false);
        when(clientService.getClient(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(new DbClient());
        when(appendService.getDbAppend(APPEND_NAME)).thenReturn(new DbAppend());
        when(toDbClientAppendMapper.map(append)).thenReturn(dbClientAppend);
        when(clientAppendRespository.save(dbClientAppend)).thenReturn(dbClientAppend);

        final Append result = clientAppendService.associateAppend(CLIENT_ID, SUB_CLIENT_ID, append);
        assertEquals(append, result);
    }

    @Test
    void testAssociateAppend_ClientAppendExists() {
        final Append append = new Append();
        append.setName(APPEND_NAME);
        when(clientAppendRespository.existsClientAppend(APPEND_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(true);

        assertThrows(HttpConflictException.class, () -> clientAppendService.associateAppend(CLIENT_ID, SUB_CLIENT_ID, append));
    }

    @Test
    void testRemoveClientsAppend() {
        final DbClientAppend dbClientAppend = new DbClientAppend();
        Set<ClientAppendView> clientAppends = new HashSet<>();
        final ClientAppendView clientAppend = new ClientAppendView();
        clientAppends.add(clientAppend);

        when(appendViewRepository.findAll(CLIENT_ID, null)).thenReturn(clientAppends);
        when(clientAppendRespository.getClientAppend(APPEND_NAME, CLIENT_ID, null)).thenReturn(dbClientAppend);

        clientAppendService.removeClientsAppend(CLIENT_ID, null, APPEND_NAME);
        verify(clientAppendRespository).delete(dbClientAppend);
    }

    @Test
    void testRemoveClientsAppendWithSubClient() {
        final DbClientAppend dbClientAppend = new DbClientAppend();
        Set<ClientAppendView> clientAppends = new HashSet<>();
        final ClientAppendView clientAppend = new ClientAppendView();
        clientAppends.add(clientAppend);

        when(appendViewRepository.findAll(CLIENT_ID, SUB_CLIENT_ID)).thenReturn(clientAppends);
        when(clientAppendRespository.getClientAppend(APPEND_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(dbClientAppend);

        clientAppendService.removeClientsAppend(CLIENT_ID, SUB_CLIENT_ID, APPEND_NAME);
        verify(clientAppendRespository).delete(dbClientAppend);
    }

    @Test
    void testRemoveClientsAppend_WhenAppendDoesNotExist() {
        when(clientAppendRespository.getClientAppend(APPEND_NAME, CLIENT_ID, null)).thenReturn(null);
        assertThrows(HttpConflictException.class, () ->  clientAppendService.removeClientsAppend(CLIENT_ID, null, APPEND_NAME));
    }

    @Test
    void testRemoveClientsAppendWithSubClient_WhenAppendDoesNotExist() {
        when(clientAppendRespository.getClientAppend(APPEND_NAME, CLIENT_ID, SUB_CLIENT_ID)).thenReturn(null);
        assertThrows(HttpConflictException.class, () ->  clientAppendService.removeClientsAppend(CLIENT_ID, SUB_CLIENT_ID, APPEND_NAME));
    }

}
