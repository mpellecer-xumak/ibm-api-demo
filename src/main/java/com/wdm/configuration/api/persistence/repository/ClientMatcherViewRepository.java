package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientMatcherViewRepository extends BaseViewRepository<ClientMatcherView, String> {
}
