package br.ufrn.imd.collectiva_backend.model;

import java.io.Serial;

import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import br.ufrn.imd.collectiva_backend.enums.RoleName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Audited
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE")
    @SequenceGenerator(name = "SEQ_ROLE", sequenceName = "seq_role", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }


    public Long getId() {
		return id;
	}


	public void setRoleId(Long id) {
		this.id = id;
	}


	public RoleName getRoleName() {
        return roleName;
    }

    public Role setRoleName(RoleName roleName) {
        this.roleName = roleName;
        return this;
    }
}
