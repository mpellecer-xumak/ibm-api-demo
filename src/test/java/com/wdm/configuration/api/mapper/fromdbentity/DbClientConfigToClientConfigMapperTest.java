package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.persistence.view.IdentityResolutionClientConfigView;

@ExtendWith(MockitoExtension.class)
class DbClientConfigToClientConfigMapperTest extends BaseMapperTest<IdentityResolutionClientConfigView, ClientConfig> {

    @Override
    protected Mapper<IdentityResolutionClientConfigView, ClientConfig> getMapper(ModelMapper modelMapper) {
        return new DbClientConfigToClientConfigMapper(modelMapper);
    }

    @Test
    void testSingleMap() {
        final IdentityResolutionClientConfigView view = getConfigView();
        final ClientConfig clientConfig = mapper.map(view);
        assertEquals(view.getKey(), clientConfig.getKey());
        assertEquals(view.getMatchLimit(), clientConfig.getMatchLimit());
        assertEquals(view.getLastNameThreshold(), clientConfig.getLastNameThreshold());
        assertEquals(view.getResidentThreshold(), clientConfig.getResidentThreshold());
        assertEquals(view.getIndividualThreshold(), clientConfig.getIndividualThreshold());
        assertEquals(view.getHouseholdThreshold(), clientConfig.getHouseholdThreshold());
        assertEquals(view.getAddressThreshold(), clientConfig.getAddressThreshold());
        assertEquals(view.getDateOfBirthRangeYears(), clientConfig.getDateOfBirthRangeYears());
        assertEquals(view.getInsightBatchLimit(), clientConfig.getInsightsBatchLimit());
        assertEquals(view.getIdentityBatchLimit(), clientConfig.getIdentityBatchLimit());
        assertEquals(view.isEnabled(), clientConfig.isEnabled());
        assertEquals(view.isStdAddressEnabled(), clientConfig.getStdAddressEnabled());
        assertEquals(view.isUseGraphId(), clientConfig.getUseGraphId());
        assertEquals(view.isEnableKeyring(), clientConfig.getEnableKeyring());
        assertEquals(view.isEnableFuzzyGenderFilter(), clientConfig.getEnableFuzzyGenderFilter());
        assertEquals(view.isEnableAdminEndpoints(), clientConfig.getEnableAdminEndpoints());
        assertEquals(view.isDpvStatusEnabled(), clientConfig.getDpvStatusEnabled());
    }

    @Test
    void testListMap() {
        final IdentityResolutionClientConfigView view = getConfigView();
        final List<ClientConfig> clientConfigs = mapper.map(List.of(view));
        final ClientConfig clientConfig = clientConfigs.get(0);
        assertEquals(view.getKey(), clientConfig.getKey());
        assertEquals(view.getMatchLimit(), clientConfig.getMatchLimit());
        assertEquals(view.getLastNameThreshold(), clientConfig.getLastNameThreshold());
        assertEquals(view.getResidentThreshold(), clientConfig.getResidentThreshold());
        assertEquals(view.getIndividualThreshold(), clientConfig.getIndividualThreshold());
        assertEquals(view.getHouseholdThreshold(), clientConfig.getHouseholdThreshold());
        assertEquals(view.getAddressThreshold(), clientConfig.getAddressThreshold());
        assertEquals(view.getDateOfBirthRangeYears(), clientConfig.getDateOfBirthRangeYears());
        assertEquals(view.getInsightBatchLimit(), clientConfig.getInsightsBatchLimit());
        assertEquals(view.getIdentityBatchLimit(), clientConfig.getIdentityBatchLimit());
        assertEquals(view.isEnabled(), clientConfig.isEnabled());
        assertEquals(view.isStdAddressEnabled(), clientConfig.getStdAddressEnabled());
        assertEquals(view.isUseGraphId(), clientConfig.getUseGraphId());
        assertEquals(view.isEnableKeyring(), clientConfig.getEnableKeyring());
        assertEquals(view.isEnableFuzzyGenderFilter(), clientConfig.getEnableFuzzyGenderFilter());
        assertEquals(view.isEnableAdminEndpoints(), clientConfig.getEnableAdminEndpoints());
        assertEquals(view.isDpvStatusEnabled(), clientConfig.getDpvStatusEnabled());
    }

    private IdentityResolutionClientConfigView getConfigView() {
        final IdentityResolutionClientConfigView view = new IdentityResolutionClientConfigView();
        view.setKey("MyKey");
        view.setMatchLimit(1);
        view.setLastNameThreshold(2);
        view.setResidentThreshold(3);
        view.setIndividualThreshold(4);
        view.setHouseholdThreshold(5);
        view.setAddressThreshold(6);
        view.setDateOfBirthRangeYears(7);
        view.setInsightBatchLimit(8);
        view.setIdentityBatchLimit(9);
        view.setEnabled(true);
        view.setStdAddressEnabled(false);
        view.setUseGraphId(true);
        view.setEnableKeyring(false);
        view.setEnableFuzzyGenderFilter(true);
        view.setEnableAdminEndpoints(false);
        view.setDpvStatusEnabled(true);
        return view;
    }

}