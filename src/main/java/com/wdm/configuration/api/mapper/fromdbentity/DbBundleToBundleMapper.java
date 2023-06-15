package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class DbBundleToBundleMapper extends Mapper<DbBundle, Bundle> {
    private final Mapper<DbAttribute, Attribute> attributeMapper;

    public DbBundleToBundleMapper(final ModelMapper modelMapper, final Mapper<DbAttribute, Attribute> attributeMapper) {
        super(modelMapper);
        this.attributeMapper = attributeMapper;
    }

    @Override
    public Class<Bundle> getTargetType() {
        return Bundle.class;
    }

    @Override
    public Bundle map(final DbBundle source) {
        final Bundle bundle = super.map(source);
        if (Objects.nonNull(attributeMapper)) {
            final List<Attribute> attributes = attributeMapper.map(source.getAttributes());
            bundle.setAttributes(attributes);
        }
        return bundle;
    }

    public Bundle mapWithoutAttributes(final DbBundle source) {
        final Bundle bundle = super.map(source);
        bundle.setAttributes(null);
        return bundle;
    }
}
