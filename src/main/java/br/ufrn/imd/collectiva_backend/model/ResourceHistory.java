package br.ufrn.imd.collectiva_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

@Audited
@Entity
@SQLRestriction("active = true")
public class ResourceHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCE_HISTORY")
    @SequenceGenerator(name = "SEQ_RESOURCE_HISTORY", sequenceName = "seq_resource_history", allocationSize = 1)
    private Long id;

    private String description;

    public ResourceHistory() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
