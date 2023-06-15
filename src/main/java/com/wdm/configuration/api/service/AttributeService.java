package com.wdm.configuration.api.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbAttributeToAttributeMapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.repository.AttributeRepository;
import org.modelmapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
@SuppressWarnings({"PMD.AvoidUncheckedExceptionsInSignatures"})
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final ModelMapper modelMapper;
    private final DbAttributeToAttributeMapper attributesMapper;

    public List<DbAttribute> createAttributes(final List<Attribute> attributes) {
        if(CollectionUtils.isEmpty(attributes)) {
            return Collections.emptyList();
        }
        final List<String> attrNames = attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.toList());
        final List<DbAttribute> existingAttributes = getAttributesByNames(attrNames);
        final Set<String> existingAttributeNames = existingAttributes.stream()
                .map(DbAttribute::getName)
                .collect(Collectors.toSet());
        final List<DbAttribute> newAttributes = attrNames.stream()
                .filter(Predicate.not(existingAttributeNames::contains))
                .map(DbAttribute::new)
                .collect(Collectors.toList());
        final List<DbAttribute> createdAttributes = attributeRepository.saveAll(newAttributes);
        return Stream.concat(existingAttributes.stream(), createdAttributes.stream()).collect(Collectors.toList());
    }

    public List<Attribute> getAttributes() {
        List<DbAttribute> dbAttributes = attributeRepository.findAll();
        if (CollectionUtils.isEmpty(dbAttributes)) {
            final String message = "No attributes were found";
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return attributesMapper.map(dbAttributes);
    }

    public List<DbAttribute> getAttributesByNames(final List<String> names) {
       return attributeRepository.findByNameIn(names); 
    }

    public Attribute deleteAttribute(final String attributeName)
            throws HttpConflictException, HttpNotFoundException {
        final DbAttribute dbAttribute = attributeRepository
                .findByName(attributeName)
                .orElseThrow(() -> new HttpNotFoundException("Attribute "+attributeName+" Not found."));

        if (dbAttribute.getBundles() != null && !dbAttribute.getBundles().isEmpty()) {
            throw new HttpConflictException("The attribute "+attributeName+" belongs to at least one bundle.");
        }
        attributeRepository.delete(dbAttribute);
        return modelMapper.map(dbAttribute, Attribute.class);
    }
}
