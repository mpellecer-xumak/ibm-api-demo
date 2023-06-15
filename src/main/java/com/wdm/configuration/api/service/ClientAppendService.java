package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.persistence.repository.BaseViewRepository;
import com.wdm.configuration.api.persistence.view.ClientAppendView;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.persistence.entity.DbClientAppend;
import com.wdm.configuration.api.persistence.repository.ClientAppendRespository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientAppendService {
    private final ClientService clientService;
    private final AppendService appendService;
    private final ClientAppendRespository clientAppendRespository;
    private final Mapper<Append, DbClientAppend> toDbClientAppendMapper;
    private final Mapper<ClientAppendView, Append> appendMapper;
    private final BaseViewRepository<ClientAppendView, String> appendViewRepository;

    public Append associateAppend(final String clientId, final String subClientId, final Append append) {
        final boolean clientAppendExists =
                clientAppendRespository.existsClientAppend(append.getName(), clientId, subClientId);
        if (clientAppendExists) {
            final String message = "The append is already associated to the client";
            log.warn(message, append.getName());
            throw new HttpConflictException(message);
        }
        final DbClient dbClient = clientService.getClient(clientId, subClientId);
        final DbAppend dbAppend = appendService.getDbAppend(append.getName());
        final DbClientAppend dbClientAppend = toDbClientAppendMapper.map(append);
        dbClientAppend.setClient(dbClient);
        dbClientAppend.setAppend(dbAppend);
        clientAppendRespository.save(dbClientAppend);
        return append;
    }

    public List<Append> updateAssociatedAppend(final String clientId, final String subClientId, final Append append) {
        DbClientAppend clientAppend = getDbClientAppend(clientId, subClientId, append.getName());
        final DbClient dbClient = clientAppend.getClient();
        final DbAppend dbAppend = clientAppend.getAppend();
        final int id = clientAppend.getId();
        clientAppend = toDbClientAppendMapper.map(append);
        clientAppend.setId(id);
        clientAppend.setClient(dbClient);
        clientAppend.setAppend(dbAppend);
        clientAppendRespository.save(clientAppend);
        return getAppendsClientConfig(clientId, subClientId);
    }

    public List<Append> removeClientsAppend(final String clientId, final String subClientId, final String appendName) {
        DbClientAppend clientAppend = getDbClientAppend(clientId, subClientId, appendName);
        clientAppendRespository.delete(clientAppend);
        return getAppendsClientConfig(clientId, subClientId);
    }

    private DbClientAppend getDbClientAppend(final String clientId, final String subClientId, final String appendName) {
        DbClientAppend clientAppend = clientAppendRespository.getClientAppend(appendName, clientId, subClientId);
        if (clientAppend == null) {
            final String message = String.format("The append %s is not associated to the client", appendName);
            log.warn(message);
            throw new HttpConflictException(message);
        }
        return clientAppend;
    }

    public List<Append> getAppendsClientConfig(final String clientId, final String subClientId) {
        final Set<ClientAppendView> clientAppends = appendViewRepository.findAll(clientId, subClientId);
        if (CollectionUtils.isEmpty(clientAppends)) {
            throw new HttpNotFoundException("Client does not have appends associated");
        }
        return appendMapper.map(clientAppends);
    }
}
