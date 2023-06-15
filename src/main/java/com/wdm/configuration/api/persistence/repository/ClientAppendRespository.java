package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbClientAppend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAppendRespository extends JpaRepository<DbClientAppend, Integer> {
    boolean existsByAppend_NameAndClient_ClientIdAndClient_SubClientId(final String appendName,
            final String clientId,
            final String subClientId);

    default boolean existsClientAppend(final String appendName, final String clientId, final String subClientId) {
        return this.existsByAppend_NameAndClient_ClientIdAndClient_SubClientId(appendName, clientId, subClientId);
    }

    DbClientAppend findByAppend_NameAndClient_ClientIdAndClient_SubClientId(
            final String aggregateName,
            final String clientId,
            final String subClientId);

    default DbClientAppend getClientAppend(final String aggregateName,
                                                 final String clientId,
                                                 final String subClientId) {
        return this.findByAppend_NameAndClient_ClientIdAndClient_SubClientId(aggregateName, clientId, subClientId);
    }
}
