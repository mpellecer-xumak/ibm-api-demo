package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAggregateViewRepository extends BaseViewRepository<ClientAggregateView, String> {
}
