package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.view.ClientSuppressionView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbSuppressionViewToSuppressionMapper extends Mapper<ClientSuppressionView, Suppression> {
    protected DbSuppressionViewToSuppressionMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Suppression> getTargetType() {
        return Suppression.class;
    }

}
