package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.view.ClientSuppressionView;

@ExtendWith({MockitoExtension.class})
class DbSuppressionToSuppressionMapperTest extends BaseMapperTest<ClientSuppressionView, Suppression> {

    @Override
    protected Mapper<ClientSuppressionView, Suppression> getMapper(ModelMapper modelMapper) {
        return new DbSuppressionViewToSuppressionMapper(modelMapper);
    }

    @Test
    void testMapSingleItem() {
        final ClientSuppressionView clientSuppressionView = getSuppressionView();
        final Suppression suppression = mapper.map(clientSuppressionView);

        assertEquals(clientSuppressionView.getName(), suppression.getName());
        assertEquals(clientSuppressionView.getAddressThreshold(), suppression.getAddressThreshold());
        assertEquals(clientSuppressionView.getDateOfBirthRangeYears(), suppression.getDateOfBirthRangeYears());
        assertEquals(clientSuppressionView.getHouseholdThreshold(), suppression.getHouseholdThreshold());
        assertEquals(clientSuppressionView.getResidentThreshold(), suppression.getResidentThreshold());
        assertEquals(clientSuppressionView.isEnabled(), suppression.isEnabled());
    }

    @Test
    void testMapList() {
        final ClientSuppressionView clientSuppressionView = getSuppressionView();
        final List<Suppression> suppressions = mapper.map(List.of(clientSuppressionView));
        final Suppression suppression = suppressions.get(0);

        assertEquals(clientSuppressionView.getName(), suppression.getName());
        assertEquals(clientSuppressionView.getAddressThreshold(), suppression.getAddressThreshold());
        assertEquals(clientSuppressionView.getDateOfBirthRangeYears(), suppression.getDateOfBirthRangeYears());
        assertEquals(clientSuppressionView.getHouseholdThreshold(), suppression.getHouseholdThreshold());
        assertEquals(clientSuppressionView.getResidentThreshold(), suppression.getResidentThreshold());
        assertEquals(clientSuppressionView.isEnabled(), suppression.isEnabled());
    }

    private ClientSuppressionView getSuppressionView() {
        final ClientSuppressionView suppressionView = new ClientSuppressionView();
        suppressionView.setName("MySuppression");
        suppressionView.setAddressThreshold(100);
        suppressionView.setEnabled(Boolean.TRUE);
        suppressionView.setDateOfBirthRangeYears(10);
        suppressionView.setHouseholdThreshold(110);
        suppressionView.setResidentThreshold(50);
        return suppressionView;
    }
}