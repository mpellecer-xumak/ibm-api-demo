package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.BundleAttribute;
import com.wdm.configuration.api.service.BundlesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BundlesController.class)
public class BundlesControllerTest {

    private static final String BASE_PATH = "/config/bundles";
    private static final String BUNDLE_NAME = "myBundle";
    private static final String ATTRIBUTE_NAME = "myAttribute";
    private static final String ATTRIBUTE_NAME_2 = "myAttribute2";
    private static final String ERROR_MESSAGE = "string";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";

    @MockBean
    private BundlesService bundlesService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRemoveAttributeBundle() throws Exception {
        final String endpointUri = BASE_PATH + "/{bundleName}/attributes/{attributeName}";

        when(bundlesService.removeAttributeBundle(BUNDLE_NAME, ATTRIBUTE_NAME)).thenReturn(getBundleWithAttributes());
        MockHttpServletRequestBuilder content = delete(endpointUri, BUNDLE_NAME, ATTRIBUTE_NAME);

        mockMvc.perform(content)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundle").value(BUNDLE_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.attributes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attributes[0].attributeName").exists());
    }


    @Test
    void testRemoveAttributeBundleWithThrow() throws Exception {
        final String endpointUri = BASE_PATH + "/{bundleName}/attributes/{attributeName}";

        when(bundlesService.removeAttributeBundle(BUNDLE_NAME, ATTRIBUTE_NAME)).thenThrow(new HttpNotFoundException(ERROR_MESSAGE));
        MockHttpServletRequestBuilder content = delete(endpointUri, BUNDLE_NAME, ATTRIBUTE_NAME);
        mockMvc.perform(content).andExpect(status().isNotFound());
    }

    @Test
    void testGetBundles() throws Exception {
        when(bundlesService.getBundleNames()).thenReturn(List.of(this.getBundle()));

        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bundleList[0].name").value(BUNDLE_NAME));
    }

    @Test
    void testRemovePlatformBundle() throws Exception {
        final String endpointUri = BASE_PATH + "/{bundleName}";
        final MockHttpServletRequestBuilder content = delete(endpointUri, BUNDLE_NAME);
        mockMvc.perform(content).andExpect(status().isOk());

    }

    @Test
    void testRemovePlatformBundleNotFound() throws Exception {
        final String endpointUri = BASE_PATH + "/{bundleName}";
        final MockHttpServletRequestBuilder content = delete(endpointUri, BUNDLE_NAME);
        doThrow(new HttpNotFoundException(EXCEPTION_MESSAGE)).when(bundlesService).deleteBundle(BUNDLE_NAME);
        final String response = mockMvc.perform(content).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
        verify(bundlesService).deleteBundle(BUNDLE_NAME);
    }

    @Test
    void testRemovePlatformBundleConflict() throws Exception {
        final String endpointUri = BASE_PATH + "/{bundleName}";
        final MockHttpServletRequestBuilder content = delete(endpointUri, BUNDLE_NAME);
        doThrow(new HttpConflictException(EXCEPTION_MESSAGE)).when(bundlesService).deleteBundle(BUNDLE_NAME);
        final String response = mockMvc.perform(content).andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(EXCEPTION_MESSAGE));
        verify(bundlesService).deleteBundle(BUNDLE_NAME);
    }

    private Bundle getBundleWithAttributes() {
        Bundle bundle = new Bundle(BUNDLE_NAME);
        Attribute attribute = new Attribute(ATTRIBUTE_NAME);
        Attribute attribute2 = new Attribute(ATTRIBUTE_NAME_2);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);
        attributes.add(attribute2);
        bundle.setAttributes(attributes);
        return bundle;
    }

    public Bundle getBundleWithAttributesPersistence() {
        final var bundle = new Bundle();
        bundle.setName(BUNDLE_NAME);
        final var bundleAttribute = new BundleAttribute();
        bundleAttribute.setBundleAttributeName(ATTRIBUTE_NAME);
        final var bundleAttribute2 = new BundleAttribute();
        bundleAttribute2.setBundleAttributeName(ATTRIBUTE_NAME_2);
        Set<BundleAttribute> bundleAttributes = new HashSet<>();
        bundleAttributes.add(bundleAttribute);
        bundleAttributes.add(bundleAttribute2);
        bundle.setBundleAttributes(bundleAttributes);
        return bundle;
    }

    private Bundle getBundle() {
        return new Bundle(BUNDLE_NAME, null, null, null, null, null);
    }

}
