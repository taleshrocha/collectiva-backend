package br.ufrn.imd.collectiva_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.Collections;
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

    @OneToMany
    private List<ResourceHistory> resourceHistory = Collections.emptyList();

    private Long bannerId;

    @ManyToOne
    private Event event;

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

    public List<ResourceHistory> getResourceHistory() {
        return resourceHistory;
    }

    public void setResourceHistory(List<ResourceHistory> resourceHistory) {
        this.resourceHistory = resourceHistory;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
