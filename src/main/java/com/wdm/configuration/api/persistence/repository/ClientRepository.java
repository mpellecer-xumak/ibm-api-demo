package com.wdm.configuration.api.persistence.repository;

import com.wdm.configuration.api.persistence.entity.DbClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<DbClient, Integer> {
    DbClient findByClientId(final String clientId);

    DbClient findByClientIdAndSubClientId(final String clientId, final String subClientId);
}
