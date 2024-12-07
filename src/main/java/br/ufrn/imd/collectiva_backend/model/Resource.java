package br.ufrn.imd.collectiva_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.List;

@Audited
@Entity
@SQLRestriction("active = true")
public class Resource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCE")
    @SequenceGenerator(name = "SEQ_RESOURCE", sequenceName = "seq_resource", allocationSize = 1)
    private Long id;

    private String name;

    private String description;

    private String log;

    private Long bannerId;

    // TODO adicionar o evento em que está alocado

    public Resource() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }
}
