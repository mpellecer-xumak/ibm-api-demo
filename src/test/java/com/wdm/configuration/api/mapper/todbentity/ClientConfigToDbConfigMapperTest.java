package com.wdm.configuration.api.mapper.todbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.PlatformConfig;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolutionClientConfig;

public class ClientConfigToDbConfigMapperTest extends BaseMapperTest<PlatformConfig, DbIdentityResolutionClientConfig> {
    private static final int ADDR_THRESHOLD = 90;
    private static final int IDENTITY_BATCH_LIMIT = 91;
    private static final int INSIGHT_BATCH_LIMIT = 92;

    @Override
    protected Mapper<PlatformConfig, DbIdentityResolutionClientConfig> getMapper(final ModelMapper modelMapper) {
        return new ClientConfigToDbConfigMapper(modelMapper);
    }

    @Test
    void testMap() {
        final PlatformConfig platformConfig = getPlatformConfig();
        final DbIdentityResolutionClientConfig irConfig = mapper.map(platformConfig);

        assertEquals(platformConfig.getInsightsBatchLimit(), irConfig.getInsightBatchLimit());
        assertEquals(platformConfig.getAddressThreshold(), irConfig.getAddressThreshold());
        assertEquals(platformConfig.getIdentityBatchLimit(), irConfig.getIdentityBatchLimit());
    }

    private PlatformConfig getPlatformConfig() {
        return PlatformConfig.builder()
                .addressThreshold(ADDR_THRESHOLD)
                .identityBatchLimit(IDENTITY_BATCH_LIMIT)
                .insightsBatchLimit(INSIGHT_BATCH_LIMIT)
                .build();
    }

}
