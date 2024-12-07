package br.ufrn.imd.collectiva_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "refresh_token")
@Where(clause = "active = true")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REFRESH_TOKEN")
    @SequenceGenerator(name = "SEQ_REFRESH_TOKEN", sequenceName = "seq_refresh_token", allocationSize = 1)
    private Long id;

    @Column(nullable = false, columnDefinition="text", length=2000)
    @NotBlank(message = "O token de recuperação é obrigatório.")
    private String token;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserInfo user;

    public RefreshToken() {
    }

    public RefreshToken(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
