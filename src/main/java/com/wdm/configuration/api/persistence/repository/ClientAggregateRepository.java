package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbClientAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientAggregateRepository extends JpaRepository<DbClientAggregate, Integer> {
    List<DbClientAggregate> findAllByClientId(final Integer id);

    List<DbClientAggregate> findAllByAggregate(final DbAggregate aggregate);

    DbClientAggregate findByAggregate_NameAndClient_ClientIdAndClient_SubClientId(
            final String aggregateName,
            final String clientId,
            final String subClientId);

    default DbClientAggregate getClientAggregate(final String aggregateName,
            final String clientId,
            final String subClientId) {
        return this.findByAggregate_NameAndClient_ClientIdAndClient_SubClientId(aggregateName, clientId, subClientId);
    }
}
