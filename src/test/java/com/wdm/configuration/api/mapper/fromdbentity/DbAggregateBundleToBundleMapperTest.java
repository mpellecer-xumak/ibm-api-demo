package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;

class DbAggregateBundleToBundleMapperTest extends BaseMapperTest<ClientAggregateView, Bundle> {
    private static final String BUNDLE_1 = "bundle1";
    private static final String BUNDLE_2 = "bundle2";
    private static final String ATTR_1_BUNDLE_1_1 = "attr1 bundle1 1";
    private static final String ATTR_1_BUNDLE_1_2 = "attr1 bundle1 2";
    private static final String ATTR_1_BUNDLE_2_1 = "attr1 bundle2 1";

    private List<Bundle> bundles;

    @Override
    protected Mapper<ClientAggregateView, Bundle> getMapper(final ModelMapper modelMapper) {
        return new DbAggregateBundleToBundleMapper(modelMapper);
    }

    @BeforeEach
    void initialize() {
        final ClientAggregateView view = getClientAggregateView(BUNDLE_1, ATTR_1_BUNDLE_1_1);
        final ClientAggregateView view2 = getClientAggregateView(BUNDLE_1, ATTR_1_BUNDLE_1_2);
        final ClientAggregateView view3 = getClientAggregateView(BUNDLE_2, ATTR_1_BUNDLE_2_1);
        final List<ClientAggregateView> viewList = List.of(view, view2, view3);
        bundles = mapper.map(viewList);
    }

    @Test
    void testExpectedBundlesAmount() {
        assertEquals(2, bundles.size());
    }

    @Test
    void testBundlesHaveItsRespectiveAttributesAmount() {
        final Bundle bundle1 = bundles.get(0);
        final Bundle bundle2 = bundles.get(1);
        assertEquals(1, bundle1.getAttributes().size());
        assertEquals(2, bundle2.getAttributes().size());
    }

    @Test
    void testBundlesAttributesAssignedCorrectly() {
        final Bundle bundle1 = bundles.get(1);
        final Bundle bundle2 = bundles.get(0);
        for (final Attribute attribute : bundle1.getAttributes()) {
            assertTrue(attribute.getName().contains(BUNDLE_1));
        }
        for (final Attribute attribute : bundle2.getAttributes()) {
            assertTrue(attribute.getName().contains(BUNDLE_2));
        }
    }

    private ClientAggregateView getClientAggregateView(final String bundleName, final String attributeName) {
        final ClientAggregateView view = new ClientAggregateView();
        view.setAggregateName("MyAggregate");
        view.setBundleName(bundleName);
        view.setAttributeName(attributeName);
        view.setEnabled(Boolean.TRUE);
        return view;
    }
}