package com.wdm.configuration.api.service;

import java.util.List;
import java.util.Objects;

import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.springframework.stereotype.Service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.repository.AppendRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@AllArgsConstructor
public class AppendService {
    private final AppendRepository appendRepository;
    private final Mapper<Append, DbAppend> toDbAppendMapper;
    private final Mapper<DbAppend, Append> toAppendMapper;

    public Append createAppend(final Append append) {
        final boolean appendExists = appendRepository.existsByName(append.getName());
        if (appendExists) {
            final String message = "The specified append already exists";
            log.warn(message);
            throw new HttpConflictException(message);
        }
        final DbAppend dbAppend = toDbAppendMapper.map(append);
        appendRepository.save(dbAppend);
        return append;
    }

    public List<Append> getAppends() {
        final List<DbAppend> dbAppends = appendRepository.findAll();
        if (CollectionUtils.isEmpty(dbAppends)) {
            final String message = "No appends found in database";
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return toAppendMapper.map(dbAppends);
    }

    public DbAppend getDbAppend(final String name) {
        final DbAppend dbAppend = appendRepository.findByName(name);
        if (Objects.isNull(dbAppend)) {
            final String message = "The specified append doesn't exist";
            log.warn(message, name);
            throw new HttpNotFoundException(message);
        }
        return dbAppend;
    }

    public void deleteAppend(final String appendName) {
        final DbAppend append = getDbAppend(appendName);
        if (!CollectionUtils.isEmpty(append.getClientAppends())) {
            throw new HttpConflictException(String.format("Cannot delete append '%s' while in use by client", appendName));
        }
        appendRepository.delete(append);
    }

    public Append updateAppend(final Append append) {
        final DbAppend dbAppend = getDbAppend(append.getName());
        dbAppend.setEnabled(append.isEnabled());
        dbAppend.setAddressThreshold(append.getAddressThreshold());
        dbAppend.setHouseholdThreshold(append.getHouseholdThreshold());
        dbAppend.setIndividualThreshold(append.getIndividualThreshold());
        dbAppend.setResidentThreshold(append.getResidentThreshold());
        dbAppend.setLastNameThreshold(append.getLastNameThreshold());
        dbAppend.setDateOfBirthRangeYears(append.getDateOfBirthRangeYears());
        dbAppend.setSuppressStateDoNotCall(append.getSuppressStateDoNotCall());
        dbAppend.setSuppressFederalDoNotCall(append.getSuppressFederalDoNotCall());
        dbAppend.setReturnDoNotCallPhone(append.getReturnDoNotCallPhone());
        dbAppend.setCheckDoNotCall(append.getCheckDoNotCall());
        dbAppend.setEnableFuzzyGenderFilter(append.getEnableFuzzyGenderFilter());
        dbAppend.setEnableHouseholdLandline(append.getEnableHouseholdLandline());
        dbAppend.setEnableHouseholdWireless(append.getEnableHouseholdWireless());
        dbAppend.setEnableIndividualLandline(append.getEnableIndividualLandline());
        dbAppend.setEnableIndividualWireless(append.getEnableIndividualWireless());
        dbAppend.setEnableContactPointLandline(append.getEnableContactPointLandline());
        dbAppend.setEnableContactPointWireless(append.getEnableContactPointWireless());
        appendRepository.save(dbAppend);
        return append;
    }
}
