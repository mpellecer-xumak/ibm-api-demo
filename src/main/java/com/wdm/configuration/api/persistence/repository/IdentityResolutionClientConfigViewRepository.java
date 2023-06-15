package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.IdentityResolutionClientConfigView;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityResolutionClientConfigViewRepository extends BaseViewRepository<IdentityResolutionClientConfigView, String> {
}
