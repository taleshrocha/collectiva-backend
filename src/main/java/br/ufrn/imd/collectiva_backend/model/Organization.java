package br.ufrn.imd.collectiva_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.List;

@Audited
@Entity
@SQLRestriction("active = true")
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORGANIZATIONS")
    @SequenceGenerator(name = "SEQ_ORGANIZATIONS", sequenceName = "seq_organizations", allocationSize = 1)
    private Long id;

    private String name;

    @ManyToMany
    private List<UserInfo> users;

    public Organization() {
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

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }
}
