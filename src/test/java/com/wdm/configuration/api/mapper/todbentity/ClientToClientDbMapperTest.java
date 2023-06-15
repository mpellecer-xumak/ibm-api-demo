package com.wdm.configuration.api.mapper.todbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbClient;
import com.wdm.configuration.api.request.ClientCreationRequest;

public class ClientToClientDbMapperTest extends BaseMapperTest<ClientCreationRequest, DbClient> {

    private static final String CLIENT_ID = "clientId";
    private static final String SUB_CLIENT_ID = "subClientId";
    private static final String CLIENT_KEY = "key";

    @Override
    protected Mapper<ClientCreationRequest, DbClient> getMapper(ModelMapper modelMapper) {
        return new ClientToClientDbMapper(modelMapper);
    }

    @Test
    void testMap() {
        final var clientRequest = getCreationRequest();
        final var dbClient = mapper.map(clientRequest);
        assertEquals(CLIENT_ID, dbClient.getClientId());
        assertEquals(SUB_CLIENT_ID, dbClient.getSubClientId());
        assertEquals(CLIENT_KEY, dbClient.getKey());
    }

    private ClientCreationRequest getCreationRequest() {
        return ClientCreationRequest.builder()
                .clientId(CLIENT_ID)
                .subClientId(SUB_CLIENT_ID)
                .key(CLIENT_KEY)
                .build();
    }

}
