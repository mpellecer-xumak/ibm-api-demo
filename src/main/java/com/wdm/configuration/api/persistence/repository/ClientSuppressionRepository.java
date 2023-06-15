package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbClientSuppression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientSuppressionRepository extends JpaRepository<DbClientSuppression, Integer> {

    DbClientSuppression findBySuppression_NameAndClient_ClientIdAndClient_SubClientId(final String suppressionName,
                                                                    final String clientId,
                                                                    final String subClientId);

    boolean existsBySuppression_NameAndClient_ClientIdAndClient_SubClientId(final String suppressionName,
                                                                       final String clientId,
                                                                       final String subClientId);

    default DbClientSuppression getClientSuppression(final String suppressionName,
                                                final String clientId,
                                                final String subClientId){
        return this.findBySuppression_NameAndClient_ClientIdAndClient_SubClientId(suppressionName, clientId, subClientId);
    }

    default boolean existsClientSuppression(final String suppressionName, final String clientId, final String subClientId) {
        return this.existsBySuppression_NameAndClient_ClientIdAndClient_SubClientId(suppressionName, clientId, subClientId);
    }
}
