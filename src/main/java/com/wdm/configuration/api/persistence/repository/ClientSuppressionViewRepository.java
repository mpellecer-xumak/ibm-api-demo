package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.ClientSuppressionView;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientSuppressionViewRepository extends BaseViewRepository<ClientSuppressionView, String> {
}
