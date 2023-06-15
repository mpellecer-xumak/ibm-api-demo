package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.entity.DbSuppression;
import com.wdm.configuration.api.persistence.repository.SuppressionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SuppressionsService {
    private final Mapper<DbSuppression, Suppression> toSuppressionMapper;
    private final Mapper<Suppression, DbSuppression> toDbSuppressionMapper;
    private final SuppressionRepository suppressionRepository;

    public List<Suppression> getAllSuppressions() {
        final List<DbSuppression> dbSuppressions = suppressionRepository.findAll();
        return toSuppressionMapper.map(dbSuppressions);
    }

    public List<Suppression> createSuppressions(final List<Suppression> suppressions) {
        if (CollectionUtils.isEmpty(suppressions)) {
            return Collections.emptyList();
        }
        final List<DbSuppression> dbSuppressions = toDbSuppressionMapper.map(suppressions);
        final List<DbSuppression> savedInstances = suppressionRepository.saveAll(dbSuppressions);
        if (CollectionUtils.isEmpty(savedInstances)) {
            return Collections.emptyList();
        }
        final Set<String> savedInstancesNames = savedInstances.stream()
                .map(DbSuppression::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(savedInstancesNames)) {
            return Collections.emptyList();
        }
        return suppressions.stream()
                .filter(suppression -> savedInstancesNames.contains(suppression.getName()))
                .collect(Collectors.toList());
    }

    public DbSuppression getDbSuppression(final String name) {
        final DbSuppression dbSuppression = suppressionRepository.findByName(name);
        if (Objects.isNull(dbSuppression)) {
            final String message = "The suppression does not exists";
            log.warn(message, name);
            throw new HttpNotFoundException(message);
        }
        return dbSuppression;
    }

    public void deleteSuppression(final String suppressionName) {
        final DbSuppression suppression = getDbSuppression(suppressionName);
        if (!CollectionUtils.isEmpty(suppression.getClientSuppressions())) {
            throw new HttpConflictException(String.format("Cannot delete suppression '%s' while in use by client", suppressionName));
        }
        suppressionRepository.delete(suppression);
    }

    public Suppression updateSuppression(final Suppression suppression) {
        final DbSuppression dbSuppression = getDbSuppression(suppression.getName());
        dbSuppression.setName(suppression.getName());
        dbSuppression.setEnabled(suppression.isEnabled());
        dbSuppression.setEnableFuzzyGenderFilter(suppression.getEnableFuzzyGenderFilter());
        dbSuppression.setResidentThreshold(suppression.getResidentThreshold());
        dbSuppression.setHouseholdThreshold(suppression.getHouseholdThreshold());
        dbSuppression.setDateOfBirthRangeYears(suppression.getDateOfBirthRangeYears());
        dbSuppression.setAddressThreshold(suppression.getAddressThreshold());
        suppressionRepository.save(dbSuppression);
        return suppression;
    }

}
