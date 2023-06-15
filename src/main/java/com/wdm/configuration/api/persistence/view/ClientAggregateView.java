package com.wdm.configuration.api.persistence.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Immutable
@IdClass(ClientAggregateViewId.class)
@Table(name = "client_aggregate_view")
public class ClientAggregateView extends BaseView {
    private static final long serialVersionUID = 2356481954862156393L;

    @Id
    private String id;

    @Id
    @Column
    private String aggregateName;

    @Column
    private String bundleName;

    @Column
    private String attributeName;

    @Column
    private String batchFileProcessingBundle;

    @Column
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientAggregateView that = (ClientAggregateView) o;

        if (id != that.id) return false;
        if (!aggregateName.equals(that.aggregateName)) return false;
        if (!Objects.equals(bundleName, that.bundleName)) return false;
        return Objects.equals(attributeName, that.attributeName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + aggregateName.hashCode();
        result = 31 * result + (bundleName != null ? bundleName.hashCode() : 0);
        result = 31 * result + (attributeName != null ? attributeName.hashCode() : 0);
        return result;
    }
}
