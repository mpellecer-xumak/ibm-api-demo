package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.MethodSetter;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolution;
import com.wdm.configuration.api.persistence.repository.IdentityResolutionRepository;
import com.wdm.configuration.api.request.IdentityRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class IdentityResolutionService {
    private IdentityResolutionRepository identityResolutionRepository;
    private final Mapper<DbIdentityResolution, IdentityRequest> mapper;

    public List<IdentityRequest>  getIdentityResolutionConfiguration(){
        final List<DbIdentityResolution> identityResolutionConfigs = identityResolutionRepository.findAll();
        if (CollectionUtils.isEmpty(identityResolutionConfigs)){
            throw new HttpNotFoundException("Configuration not found");
        }
        return mapper.map(identityResolutionConfigs);
    }

    public IdentityRequest putProvidedConfiguration(final IdentityRequest config) {
        if (Objects.isNull(config)) {
            throw new HttpConflictException("New config was empty");
        }
        final List<DbIdentityResolution> dbConfigList = identityResolutionRepository.findAll();
        final DbIdentityResolution dbConfig = dbConfigList.get(0);
        MethodSetter.setValue(config.getMatchLimit(), dbConfig::setMatchLimit);
        MethodSetter.setValue(config.getLastNameThreshold(), dbConfig::setLastNameThreshold);
        MethodSetter.setValue(config.getResidentThreshold(), dbConfig::setResidentThreshold);
        MethodSetter.setValue(config.getIndividualThreshold(), dbConfig::setIndividualThreshold);
        MethodSetter.setValue(config.getHouseholdThreshold(), dbConfig::setHouseholdThreshold);
        MethodSetter.setValue(config.getAddressThreshold(), dbConfig::setAddressThreshold);
        MethodSetter.setValue(config.getDateOfBirthRangeYears(), dbConfig::setDateOfBirthRangeYears);
        MethodSetter.setValue(config.getInsightsBatchLimit(), dbConfig::setInsightBatchLimit);
        MethodSetter.setValue(config.getIdentityBatchLimit(), dbConfig::setIdentityBatchLimit);
        MethodSetter.setValue(config.getStdAddressEnabled(), dbConfig::setStdAddressEnabled);
        MethodSetter.setValue(config.getUseGraphId(), dbConfig::setUseGraphId);
        MethodSetter.setValue(config.getEnableKeyring(), dbConfig::setEnableKeyring);
        MethodSetter.setValue(config.getEnableFuzzyGenderFilter(), dbConfig::setEnableFuzzyGenderFilter);
        MethodSetter.setValue(config.getDpvStatusEnabled(), dbConfig::setDpvStatusEnabled);
        MethodSetter.setValue(config.getEnableAdminEndpoints(), dbConfig::setEnableAdminEndpoints);
        final DbIdentityResolution result = identityResolutionRepository.save(dbConfig);
        return mapper.map(result);
    }
}
