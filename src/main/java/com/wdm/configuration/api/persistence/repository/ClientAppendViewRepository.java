package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.ClientAppendView;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAppendViewRepository extends BaseViewRepository<ClientAppendView, String> {
}
