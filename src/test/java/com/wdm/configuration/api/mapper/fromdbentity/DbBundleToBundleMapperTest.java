package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.entity.DbBundle;

class DbBundleToBundleMapperTest extends BaseMapperTest<DbBundle, Bundle> {

    @Override
    protected Mapper<DbBundle, Bundle> getMapper(ModelMapper modelMapper) {
        final DbAttributeToAttributeMapper attributeMapper = new DbAttributeToAttributeMapper(modelMapper);
        return new DbBundleToBundleMapper(modelMapper, attributeMapper);
    }

    @Test
    void testSingleMap() {
        final DbBundle dbBundle = getBundle();
        final Bundle bundle = mapper.map(dbBundle);

        assertEquals(dbBundle.getName(), bundle.getName());
        assertEquals(dbBundle.getAttributes().size(), bundle.getAttributes().size());
        assertEquals(dbBundle.getAttributes().get(1).getName(), bundle.getAttributes().get(1).getName());
    }

    @Test
    void testListMap() {
        final DbBundle dbBundle = getBundle();
        final DbBundle dbBundle1 = getBundle();
        dbBundle1.getAttributes().remove(1);
        final List<DbBundle> dbBundles = List.of(dbBundle, dbBundle1);
        final List<Bundle> bundles = mapper.map(dbBundles);

        for (int i = 0; i < dbBundles.size(); i++) {
            assertEquals(dbBundles.get(i).getName(), bundles.get(i).getName());
            assertEquals(dbBundles.get(i).getAttributes().size(), bundles.get(i).getAttributes().size());
            assertEquals(dbBundles.get(i).getAttributes().get(0).getName(), bundles.get(i).getAttributes().get(0).getName());
        }
    }

    private DbBundle getBundle() {
        final DbBundle bundle = new DbBundle();
        bundle.setName("MyBundle");
        final DbAttribute attr1 = new DbAttribute();
        attr1.setName("Attr1");
        final DbAttribute attr2 = new DbAttribute();
        attr1.setName("Attr2");
        final List<DbAttribute> attributeList = new ArrayList<>(2);
        attributeList.add(attr1);
        attributeList.add(attr2);
        bundle.setAttributes(attributeList);
        return bundle;
    }
}