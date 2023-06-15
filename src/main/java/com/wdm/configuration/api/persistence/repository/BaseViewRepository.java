package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.view.BaseView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Set;

@NoRepositoryBean
public interface BaseViewRepository<T extends BaseView, ID> extends Repository<T, ID> {
    T findByClientId(final String clientId);

    T findByClientIdAndSubClientIdIsNull(final String clientId);

    T findByClientIdAndSubClientId(final String clientId, final String subClientId);

    Set<T> findAllByClientIdAndSubClientIdIsNull(final String clientId);

    Set<T> findAllByClientId(final String clientId);

    Set<T> findAllByClientIdAndSubClientId(final String clientId, final String subClientId);

    default T find(final String clientId, final String subClientId) {
        if (StringUtils.isEmpty(clientId)) {
            throw new IllegalArgumentException("Client id is required");
        }

        if (StringUtils.isEmpty(subClientId)) {
            return findByClientIdAndSubClientIdIsNull(clientId);
        }
        return findByClientIdAndSubClientId(clientId, subClientId);
    }

    default Set<T> findAll(final String clientId, final String subClientId) {
        if (StringUtils.isEmpty(clientId)) {
            throw new IllegalArgumentException("Client id is required");
        }

        if (StringUtils.isEmpty(subClientId)) {
            return findAllByClientIdAndSubClientIdIsNull(clientId);
        }
        return findAllByClientIdAndSubClientId(clientId, subClientId);
    }
}
